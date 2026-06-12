package com.forge.core.retention

import com.forge.core.model.Pillar

/**
 * The Shadow: compete against yourself 30 days ago — every score, every
 * day. Zero network effects to bootstrap, zero incentive to cheat.
 */
object Shadow {

    enum class Verdict { AHEAD, BEHIND, SPLIT }

    data class Comparison(
        val pillar: Pillar,
        val today: Double,
        val shadow: Double,
    ) {
        val delta: Double get() = today - shadow
    }

    data class Result(val comparisons: List<Comparison>, val verdict: Verdict)

    /**
     * [shadowScores] may be missing pillars (user is younger than 30 days
     * on that signal); those pillars aren't contested.
     */
    fun compare(today: Map<Pillar, Double>, shadowScores: Map<Pillar, Double>): Result {
        val comparisons = Pillar.entries.mapNotNull { p ->
            val t = today[p] ?: return@mapNotNull null
            val s = shadowScores[p] ?: return@mapNotNull null
            Comparison(p, t, s)
        }
        val wins = comparisons.count { it.delta > 0 }
        val losses = comparisons.count { it.delta < 0 }
        val verdict = when {
            comparisons.isEmpty() -> Verdict.SPLIT
            wins > 0 && losses == 0 -> Verdict.AHEAD
            losses > 0 && wins == 0 -> Verdict.BEHIND
            wins > losses -> Verdict.AHEAD
            losses > wins -> Verdict.BEHIND
            else -> Verdict.SPLIT
        }
        return Result(comparisons, verdict)
    }
}
