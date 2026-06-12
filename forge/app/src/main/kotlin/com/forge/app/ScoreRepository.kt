package com.forge.app

import com.forge.app.collect.UsageStatsCollector
import com.forge.app.data.DayScoreRow
import com.forge.app.data.ForgeDao
import com.forge.core.model.AppCategory
import com.forge.core.model.Pillar
import com.forge.core.scoring.HealthScorer
import com.forge.core.scoring.HealthSignals
import com.forge.core.scoring.MindScorer
import com.forge.core.scoring.MindSignals
import com.forge.core.scoring.Momentum
import com.forge.core.scoring.MomentumScore
import com.forge.core.scoring.ProductivityScorer
import com.forge.core.scoring.ProductivitySignals
import com.forge.core.signals.ImpulseDetector
import com.forge.core.signals.Sleep
import com.forge.core.signals.SleepTarget
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Glue between collectors and the core engine: recompute today's three day
 * scores from raw signals, persist them, and expose 7-day momentum.
 * Everything here runs on-device; nothing leaves it.
 */
class ScoreRepository(
    private val dao: ForgeDao,
    private val collector: UsageStatsCollector,
    private val zone: ZoneId = ZoneId.systemDefault(),
    private val sleepTarget: SleepTarget = SleepTarget(LocalTime.of(23, 0), LocalTime.of(6, 30)),
) {

    suspend fun momentum(pillar: Pillar): MomentumScore? {
        val rows = dao.recentDayScores(pillar.name, 7)
        if (rows.isEmpty()) return null
        return Momentum.of(rows.map { it.dayScore })
    }

    /** Recompute and store today's day scores. Called by ritual touchpoints + a periodic worker. */
    suspend fun recomputeToday(
        now: Instant = Instant.now(),
        // Signals owned by other subsystems, passed in until those land:
        checkpointsProceeded: Int = 0,
        checkpointsDeflected: Int = 0,
        monolithMinutes: Long = 0,
        monolithsVoided: Int = 0,
        contractsKept: Int = 0,
        contractsTotal: Int = 0,
        tasksCompleted: Int = 0,
        steps: Int = 0,
        stepGoal: Int = 8000,
        gymCheckIn: Boolean = false,
        verifiedWorkout: Boolean = false,
    ) {
        val today = now.atZone(zone).toLocalDate()
        val dayStart = today.atStartOfDay(zone).toInstant()
        val sessions = collector.sessionsBetween(dayStart, now)

        val drainMinutes = sessions.filter { it.category == AppCategory.DRAIN }.sumOf { it.minutes }
        val growthMinutes = sessions.filter { it.category == AppCategory.GROWTH }.sumOf { it.minutes }
        val impulses = ImpulseDetector.detect(sessions, zone)

        val unlocks = dao
            .unlocksBetween(
                today.minusDays(1).atTime(20, 0).atZone(zone).toInstant().toEpochMilli(),
                today.atTime(12, 0).atZone(zone).toInstant().toEpochMilli(),
            )
            .map { Instant.ofEpochMilli(it.atEpochMs) }
        val sleepAdherence = Sleep.deriveWindow(unlocks, zone, today)
            ?.let { Sleep.adherence(it, sleepTarget, zone) }

        val mind = MindScorer.dayScore(
            MindSignals(sleepAdherence, growthMinutes, checkpointsProceeded, checkpointsDeflected),
        )
        val productivity = ProductivityScorer.dayScore(
            ProductivitySignals(
                drainMinutes, monolithMinutes, monolithsVoided,
                contractsKept, contractsTotal, tasksCompleted, impulses.size,
            ),
        )
        val health = HealthScorer.dayScore(
            HealthSignals(steps, stepGoal, gymCheckIn, verifiedWorkout),
        )

        storeDayScore(today, Pillar.MIND, mind)
        storeDayScore(today, Pillar.PRODUCTIVITY, productivity)
        storeDayScore(today, Pillar.HEALTH, health)
    }

    private suspend fun storeDayScore(date: LocalDate, pillar: Pillar, score: Double) {
        dao.insertDayScore(DayScoreRow(epochDay = date.toEpochDay(), pillar = pillar.name, dayScore = score))
    }
}
