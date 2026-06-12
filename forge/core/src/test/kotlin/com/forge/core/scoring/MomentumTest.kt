package com.forge.core.scoring

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MomentumTest {

    private val perfectWeek = List(7) { 100.0 }

    @Test
    fun `perfect week scores 100`() {
        assertEquals(100.0, Momentum.score(perfectWeek), 1e-9)
    }

    @Test
    fun `one bad day dents but never zeroes`() {
        // Yesterday was perfect history; today was a total collapse.
        val scores = listOf(0.0) + List(6) { 100.0 }
        val s = Momentum.score(scores)
        assertEquals(70.0, s, 1e-9) // dented by exactly the top weight
        assertTrue(s > 50.0, "one bad day must not crater the score")
    }

    @Test
    fun `recovery is visible within 48 hours`() {
        val afterCollapse = listOf(0.0) + List(6) { 100.0 }
        val twoGoodDaysLater = listOf(100.0, 100.0, 0.0) + List(4) { 100.0 }
        val before = Momentum.score(afterCollapse)
        val after = Momentum.score(twoGoodDaysLater)
        assertTrue(after - before >= 10.0, "two good days must visibly lift momentum (was $before -> $after)")
        assertEquals(Trend.RISING, Momentum.trend(twoGoodDaysLater))
    }

    @Test
    fun `falling trajectory shows a falling arrow`() {
        val sliding = listOf(20.0, 30.0, 90.0, 95.0, 100.0, 100.0, 100.0)
        assertEquals(Trend.FALLING, Momentum.trend(sliding))
    }

    @Test
    fun `steady week holds`() {
        assertEquals(Trend.HOLDING, Momentum.trend(List(7) { 80.0 }))
    }

    @Test
    fun `new users renormalize over available days`() {
        assertEquals(75.0, Momentum.score(listOf(75.0)), 1e-9)
        assertEquals(80.0, Momentum.score(listOf(80.0, 80.0, 80.0)), 1e-9)
    }

    @Test
    fun `weights sum to one`() {
        assertEquals(1.0, Momentum.WEIGHTS.sum(), 1e-9)
    }
}
