package com.forge.core.retention

import com.forge.core.ledger.CheckpointOutcome
import com.forge.core.ledger.ExcuseTag
import com.forge.core.ledger.LedgerEntry
import com.forge.core.ledger.VerificationSource
import com.forge.core.model.AppCategory
import com.forge.core.model.Pillar
import com.forge.core.scoring.Trend
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals

class TribunalTest {

    private val zone = ZoneId.of("Asia/Dhaka")
    private val weekStart = LocalDate.of(2026, 6, 8)

    private fun on(day: Int, h: Int) =
        weekStart.plusDays(day.toLong()).atTime(h, 0).atZone(zone).toInstant()

    @Test
    fun `tribunal correlates tired excuses with late nights`() {
        val entries = listOf(
            LedgerEntry.ExcuseLogged(on(1, 9), ExcuseTag.TIRED, "exhausted today"),
            LedgerEntry.ExcuseLogged(on(3, 9), ExcuseTag.TIRED, "too tired"),
            LedgerEntry.ExcuseLogged(on(5, 9), ExcuseTag.TIRED, "drained"),
            LedgerEntry.ExcuseLogged(on(4, 9), ExcuseTag.BUSY, "deadline at work"),
        )
        // Two of the three "tired" mornings followed a phone-past-1am night.
        val lateNights = setOf(weekStart.plusDays(1), weekStart.plusDays(5))

        val report = Tribunal.assemble(weekStart, entries, emptyMap(), lateNights, zone)

        val tired = report.excusePatterns.first { it.tag == ExcuseTag.TIRED }
        assertEquals(3, tired.count)
        assertEquals(2, tired.precededByLateNight)
        val busy = report.excusePatterns.first { it.tag == ExcuseTag.BUSY }
        assertEquals(0, busy.precededByLateNight)
    }

    @Test
    fun `tribunal counts the week's evidence`() {
        val entries = listOf(
            LedgerEntry.PromiseMade(on(0, 8), "p1", "gym at 7", VerificationSource.GPS_GYM),
            LedgerEntry.PromiseMade(on(0, 8), "p2", "90m monolith", VerificationSource.MONOLITH),
            LedgerEntry.PromiseKept(on(0, 20), "p1", VerificationSource.GPS_GYM),
            LedgerEntry.PromiseBroken(on(1, 20), "p2"),
            LedgerEntry.MonolithVerified(on(2, 11), 120),
            LedgerEntry.MonolithVoided(on(3, 11), 90, 40),
            LedgerEntry.CheckpointEvent(on(2, 22), AppCategory.DRAIN, CheckpointOutcome.DEFLECTED, Pillar.MIND, 0),
            LedgerEntry.CheckpointEvent(on(2, 23), AppCategory.DRAIN, CheckpointOutcome.PROCEEDED, Pillar.MIND, 4),
        )
        val trends = mapOf(Pillar.MIND to Trend.RISING)

        val r = Tribunal.assemble(weekStart, entries, trends, emptySet(), zone)

        assertEquals(2, r.promisesMade)
        assertEquals(1, r.promisesKept)
        assertEquals(1, r.promisesBroken)
        assertEquals(0.5, r.promiseKeptRate, 1e-9)
        assertEquals(120, r.monolithMinutesVerified)
        assertEquals(1, r.monolithsVoided)
        assertEquals(1, r.checkpointsDeflected)
        assertEquals(1, r.checkpointsProceeded)
        assertEquals(Trend.RISING, r.trends[Pillar.MIND])
    }
}
