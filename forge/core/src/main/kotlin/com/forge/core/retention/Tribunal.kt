package com.forge.core.retention

import com.forge.core.ledger.CheckpointOutcome
import com.forge.core.ledger.ExcuseTag
import com.forge.core.ledger.LedgerEntry
import com.forge.core.model.Pillar
import com.forge.core.scoring.Trend
import java.time.LocalDate
import java.time.ZoneId

/**
 * Weekly Tribunal: the evidence report. This assembler produces the
 * structured facts; the LLM only phrases them in the coach voice.
 * Firm on failures, names the wins, surfaces excuse patterns.
 */
object Tribunal {

    data class ExcusePattern(
        val tag: ExcuseTag,
        val count: Int,
        /** How many of those excuses followed a late night on the phone. */
        val precededByLateNight: Int,
    )

    data class Report(
        val weekStart: LocalDate,
        val promisesMade: Int,
        val promisesKept: Int,
        val promisesBroken: Int,
        val monolithMinutesVerified: Long,
        val monolithsVoided: Int,
        val checkpointsDeflected: Int,
        val checkpointsProceeded: Int,
        val excusePatterns: List<ExcusePattern>,
        val trends: Map<Pillar, Trend>,
    ) {
        val promiseKeptRate: Double
            get() = if (promisesMade == 0) 0.0 else promisesKept.toDouble() / promisesMade
    }

    /**
     * [lateNights] — dates whose *preceding* night had a late-night signal
     * (impulse detection or last unlock past 01:00). An excuse logged on
     * one of those dates correlates: "'Tired' ×4. Three of those nights:
     * phone past 1am."
     */
    fun assemble(
        weekStart: LocalDate,
        entries: List<LedgerEntry>,
        trends: Map<Pillar, Trend>,
        lateNights: Set<LocalDate>,
        zone: ZoneId,
    ): Report {
        val excuses = entries.filterIsInstance<LedgerEntry.ExcuseLogged>()
        val patterns = excuses
            .groupBy { it.tag }
            .map { (tag, list) ->
                ExcusePattern(
                    tag = tag,
                    count = list.size,
                    precededByLateNight = list.count {
                        it.at.atZone(zone).toLocalDate() in lateNights
                    },
                )
            }
            .sortedByDescending { it.count }

        val checkpoints = entries.filterIsInstance<LedgerEntry.CheckpointEvent>()

        return Report(
            weekStart = weekStart,
            promisesMade = entries.count { it is LedgerEntry.PromiseMade },
            promisesKept = entries.count { it is LedgerEntry.PromiseKept },
            promisesBroken = entries.count { it is LedgerEntry.PromiseBroken },
            monolithMinutesVerified = entries
                .filterIsInstance<LedgerEntry.MonolithVerified>()
                .sumOf { it.minutes },
            monolithsVoided = entries.count { it is LedgerEntry.MonolithVoided },
            checkpointsDeflected = checkpoints.count { it.outcome == CheckpointOutcome.DEFLECTED },
            checkpointsProceeded = checkpoints.count { it.outcome == CheckpointOutcome.PROCEEDED },
            excusePatterns = patterns,
            trends = trends,
        )
    }
}
