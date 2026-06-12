package com.forge.core.scoring

import kotlin.math.min

data class MindSignals(
    /** 0..1 from Sleep.adherence; null = no signal tonight (treated as kept). */
    val sleepAdherence: Double?,
    /** Minutes in GROWTH apps (audio/reading). */
    val growthMinutes: Long,
    /** Mind-category checkpoints the user pushed through today. */
    val checkpointsProceeded: Int,
    /** Mind-category checkpoints the user walked away from. */
    val checkpointsDeflected: Int,
)

/**
 * Mind day score, 0–100.
 *  - Sleep window adherence is the backbone (60 pts).
 *  - Growth input is real but *capped* (+10 max) so passive
 *    consumption can't be farmed.
 *  - Discipline pool (30 pts): each proceeded checkpoint costs 4,
 *    deflections claw back lost ground — deflections are progress.
 */
object MindScorer {
    const val SLEEP_POINTS = 60.0
    const val GROWTH_CAP = 10.0
    const val DISCIPLINE_POINTS = 30.0
    const val PROCEED_COST = 4.0
    const val DEFLECT_CREDIT = 1.0

    /** 30 growth minutes earn 5 points; cap reached at one hour. */
    private const val GROWTH_POINTS_PER_MINUTE = 5.0 / 30.0

    fun dayScore(s: MindSignals): Double {
        val sleep = (s.sleepAdherence ?: 1.0).coerceIn(0.0, 1.0) * SLEEP_POINTS
        val growth = min(s.growthMinutes * GROWTH_POINTS_PER_MINUTE, GROWTH_CAP)
        val discipline = (DISCIPLINE_POINTS - s.checkpointsProceeded * PROCEED_COST +
                s.checkpointsDeflected * DEFLECT_CREDIT)
            .coerceIn(0.0, DISCIPLINE_POINTS)
        return (sleep + growth + discipline).coerceIn(0.0, 100.0)
    }
}
