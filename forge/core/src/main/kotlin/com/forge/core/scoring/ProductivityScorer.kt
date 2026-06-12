package com.forge.core.scoring

import kotlin.math.min

data class ProductivitySignals(
    /** Minutes in DRAIN apps today. */
    val drainMinutes: Long,
    /** Verified deep-work minutes from completed Monolith sessions. */
    val monolithMinutes: Long,
    /** Monolith sessions voided by leaving early. */
    val monolithsVoided: Int,
    /** Daily-contract promises verified kept / total confirmed. */
    val contractsKept: Int,
    val contractsTotal: Int,
    /** Self-reported tasks — deliberately a light layer. */
    val tasksCompleted: Int,
    /** Impulse patterns detected today (late-night, switch-loops, binges). */
    val impulseSignals: Int,
)

/**
 * Productivity day score, 0–100.
 * Contracts (35) + Monolith deep work (30) + distraction control (25)
 * + tasks (5, weighted low) + clean-signal bonus (5).
 * Impulse patterns and voided Monoliths subtract after the fact —
 * broken promises hurt more than never promising.
 */
object ProductivityScorer {
    const val CONTRACT_POINTS = 35.0
    const val MONOLITH_POINTS = 30.0
    const val DRAIN_POINTS = 25.0
    const val TASK_POINTS = 5.0
    const val CLEAN_BONUS = 5.0

    /** Full Monolith credit at two verified hours. */
    private const val MONOLITH_TARGET_MINUTES = 120.0

    /** Drain control hits zero at 150 drain minutes. */
    private const val DRAIN_ZERO_MINUTES = 150.0

    const val IMPULSE_PENALTY = 5.0
    const val VOID_PENALTY = 10.0

    fun dayScore(s: ProductivitySignals): Double {
        val contracts = if (s.contractsTotal == 0) {
            CONTRACT_POINTS * 0.5 // no promises made: neutral, not rewarded
        } else {
            CONTRACT_POINTS * s.contractsKept.coerceAtMost(s.contractsTotal) / s.contractsTotal
        }
        val monolith = min(s.monolithMinutes / MONOLITH_TARGET_MINUTES, 1.0) * MONOLITH_POINTS
        val drain = (1.0 - s.drainMinutes / DRAIN_ZERO_MINUTES).coerceIn(0.0, 1.0) * DRAIN_POINTS
        val tasks = min(s.tasksCompleted.toDouble(), TASK_POINTS)
        val clean = if (s.impulseSignals == 0 && s.monolithsVoided == 0) CLEAN_BONUS else 0.0

        val penalties = s.impulseSignals * IMPULSE_PENALTY + s.monolithsVoided * VOID_PENALTY
        return (contracts + monolith + drain + tasks + clean - penalties).coerceIn(0.0, 100.0)
    }
}
