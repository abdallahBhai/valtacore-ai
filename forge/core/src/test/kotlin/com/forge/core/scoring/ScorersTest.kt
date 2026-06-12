package com.forge.core.scoring

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScorersTest {

    // ---- Mind ----

    @Test
    fun `growth input is capped so passive consumption cannot be farmed`() {
        val oneHour = MindScorer.dayScore(MindSignals(1.0, 60, 0, 0))
        val sixHours = MindScorer.dayScore(MindSignals(1.0, 360, 0, 0))
        assertEquals(oneHour, sixHours, 1e-9)
        assertEquals(100.0, sixHours, 1e-9)
    }

    @Test
    fun `proceeding through checkpoints costs four each`() {
        val clean = MindScorer.dayScore(MindSignals(1.0, 0, 0, 0))
        val twoProceeds = MindScorer.dayScore(MindSignals(1.0, 0, 2, 0))
        assertEquals(8.0, clean - twoProceeds, 1e-9)
    }

    @Test
    fun `deflections claw back lost discipline ground`() {
        val proceeded = MindScorer.dayScore(MindSignals(1.0, 0, 1, 0))
        val proceededButDeflectedTwice = MindScorer.dayScore(MindSignals(1.0, 0, 1, 2))
        assertTrue(proceededButDeflectedTwice > proceeded)
    }

    @Test
    fun `no sleep signal is treated as kept, not punished`() {
        assertEquals(
            MindScorer.dayScore(MindSignals(1.0, 0, 0, 0)),
            MindScorer.dayScore(MindSignals(null, 0, 0, 0)),
            1e-9,
        )
    }

    // ---- Productivity ----

    private fun prod(
        drain: Long = 0, monolith: Long = 0, voided: Int = 0,
        kept: Int = 0, total: Int = 0, tasks: Int = 0, impulses: Int = 0,
    ) = ProductivityScorer.dayScore(
        ProductivitySignals(drain, monolith, voided, kept, total, tasks, impulses),
    )

    @Test
    fun `perfect day hits 100`() {
        assertEquals(100.0, prod(monolith = 120, kept = 3, total = 3, tasks = 5), 1e-9)
    }

    @Test
    fun `making no promises is neutral, not rewarded`() {
        assertTrue(prod(total = 0) < prod(kept = 3, total = 3))
        assertEquals(ProductivityScorer.CONTRACT_POINTS * 0.5 + ProductivityScorer.DRAIN_POINTS + ProductivityScorer.CLEAN_BONUS, prod(total = 0), 1e-9)
    }

    @Test
    fun `a voided monolith hurts more than never declaring one`() {
        assertTrue(prod(voided = 1) < prod())
        assertEquals(
            ProductivityScorer.VOID_PENALTY + ProductivityScorer.CLEAN_BONUS,
            prod() - prod(voided = 1), 1e-9,
        )
    }

    @Test
    fun `impulse signals subtract and the floor is zero`() {
        assertTrue(prod(impulses = 2) < prod())
        assertEquals(0.0, prod(drain = 500, impulses = 20), 1e-9)
    }

    @Test
    fun `tasks are weighted low`() {
        assertTrue(prod(tasks = 50) - prod() <= ProductivityScorer.TASK_POINTS)
    }

    // ---- Health ----

    @Test
    fun `health scores consistency not performance`() {
        val showedUp = HealthScorer.dayScore(HealthSignals(8000, 8000, gymCheckIn = true, verifiedWorkout = true))
        assertEquals(100.0, showedUp, 1e-9)
        // Extra steps beyond the goal earn nothing extra.
        val overachiever = HealthScorer.dayScore(HealthSignals(30000, 8000, gymCheckIn = true, verifiedWorkout = true))
        assertEquals(showedUp, overachiever, 1e-9)
    }

    @Test
    fun `partial steps earn proportional credit`() {
        assertEquals(20.0, HealthScorer.dayScore(HealthSignals(4000, 8000, false, false)), 1e-9)
    }
}
