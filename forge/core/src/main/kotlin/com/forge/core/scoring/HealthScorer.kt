package com.forge.core.scoring

import kotlin.math.min

data class HealthSignals(
    val steps: Int,
    val stepGoal: Int,
    /** GPS-verified gym attendance today. */
    val gymCheckIn: Boolean,
    /** Camera-verified workout completed today (any of the V1 exercises). */
    val verifiedWorkout: Boolean,
)

/**
 * Health day score, 0–100. Health scores *consistency*, not performance:
 * showing up (steps 40, gym 30, verified workout 30) is the whole game.
 * Rep counts feed share cards, never the score.
 */
object HealthScorer {
    const val STEP_POINTS = 40.0
    const val GYM_POINTS = 30.0
    const val WORKOUT_POINTS = 30.0

    fun dayScore(s: HealthSignals): Double {
        require(s.stepGoal > 0) { "step goal must be positive" }
        val steps = min(s.steps.toDouble() / s.stepGoal, 1.0) * STEP_POINTS
        val gym = if (s.gymCheckIn) GYM_POINTS else 0.0
        val workout = if (s.verifiedWorkout) WORKOUT_POINTS else 0.0
        return (steps + gym + workout).coerceIn(0.0, 100.0)
    }
}
