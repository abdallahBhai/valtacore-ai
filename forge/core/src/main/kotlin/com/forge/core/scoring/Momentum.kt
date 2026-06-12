package com.forge.core.scoring

enum class Trend { RISING, HOLDING, FALLING }

data class MomentumScore(val value: Double, val trend: Trend)

/**
 * Rolling 7-day weighted momentum. Design constraints from the spec:
 *  - one bad day dents the score, it never zeroes it;
 *  - recovery is visible within ~48 hours of getting back on track;
 *  - a trajectory arrow is shown alongside the number.
 */
object Momentum {

    /** Most recent day first. Sums to 1.0. */
    val WEIGHTS = listOf(0.30, 0.20, 0.15, 0.12, 0.09, 0.08, 0.06)

    private const val TREND_DEADBAND = 1.5

    /**
     * [dayScores] are 0–100 day scores, most recent first. Fewer than seven
     * days (new users, post-Comeback) renormalizes over the days that exist.
     */
    fun score(dayScores: List<Double>): Double {
        require(dayScores.isNotEmpty()) { "need at least one day" }
        val days = dayScores.take(WEIGHTS.size)
        val weights = WEIGHTS.take(days.size)
        val weightSum = weights.sum()
        return days.zip(weights) { s, w -> s.coerceIn(0.0, 100.0) * w }.sum() / weightSum
    }

    /** Trajectory: today's window vs the window as of two days ago. */
    fun trend(dayScores: List<Double>): Trend {
        if (dayScores.size < 3) return Trend.HOLDING
        val now = score(dayScores)
        val twoDaysAgo = score(dayScores.drop(2))
        return when {
            now - twoDaysAgo > TREND_DEADBAND -> Trend.RISING
            twoDaysAgo - now > TREND_DEADBAND -> Trend.FALLING
            else -> Trend.HOLDING
        }
    }

    fun of(dayScores: List<Double>): MomentumScore =
        MomentumScore(score(dayScores), trend(dayScores))
}
