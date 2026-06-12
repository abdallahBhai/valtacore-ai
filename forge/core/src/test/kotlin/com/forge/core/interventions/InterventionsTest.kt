package com.forge.core.interventions

import com.forge.core.model.AppCategory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InterventionsTest {

    private val t0: Instant = Instant.parse("2026-06-12T08:00:00Z")

    // ---- Iron mode ----

    @Test
    fun `iron blocks restricted categories for the committed duration`() {
        val iron = IronMode()
        iron.commit(t0, Duration.ofDays(7), setOf(AppCategory.DRAIN))
        assertTrue(iron.isBlocking(AppCategory.DRAIN, t0.plus(Duration.ofDays(3))))
        assertFalse(iron.isBlocking(AppCategory.TOOL, t0.plus(Duration.ofDays(3))))
        assertFalse(iron.isBlocking(AppCategory.DRAIN, t0.plus(Duration.ofDays(8))))
    }

    @Test
    fun `early exit only unlocks after the 24 hour cooldown`() {
        val iron = IronMode(earlyExitCost = 10)
        iron.commit(t0, Duration.ofDays(7), setOf(AppCategory.DRAIN))

        val effective = iron.requestEarlyExit(t0.plus(Duration.ofDays(1)))
        assertEquals(t0.plus(Duration.ofDays(2)), effective)

        // Still blocking during cooldown — the urge passes before the unlock does.
        assertTrue(iron.isBlocking(AppCategory.DRAIN, t0.plus(Duration.ofHours(30))))
        assertThrows<IllegalStateException> {
            iron.confirmEarlyExit(t0.plus(Duration.ofHours(30)))
        }

        val brokenEntry = iron.confirmEarlyExit(t0.plus(Duration.ofDays(2)))
        assertEquals(10, brokenEntry.scoreCost)
        assertFalse(iron.isBlocking(AppCategory.DRAIN, t0.plus(Duration.ofDays(2))))
    }

    @Test
    fun `cancelling the exit request keeps the commitment standing`() {
        val iron = IronMode()
        iron.commit(t0, Duration.ofDays(7), setOf(AppCategory.DRAIN))
        iron.requestEarlyExit(t0.plus(Duration.ofDays(1)))
        iron.cancelExitRequest()
        assertTrue(iron.state is IronMode.State.Active)
        assertTrue(iron.isBlocking(AppCategory.DRAIN, t0.plus(Duration.ofDays(5))))
    }

    // ---- Monolith ----

    @Test
    fun `completing a monolith verifies the declared deep work`() {
        val m = MonolithSession(declaredMinutes = 90, startedAt = t0)
        assertNull(m.complete(t0.plus(Duration.ofMinutes(89)))) // not yet
        val verified = m.complete(t0.plus(Duration.ofMinutes(90)))
        assertNotNull(verified)
        assertEquals(90, verified.minutes)
        assertEquals(MonolithSession.Status.VERIFIED, m.status)
    }

    @Test
    fun `leaving early voids the session and partial time earns nothing`() {
        val m = MonolithSession(declaredMinutes = 90, startedAt = t0)
        val voided = m.abandon(t0.plus(Duration.ofMinutes(75)))
        assertEquals(MonolithSession.Status.VOIDED, m.status)
        assertEquals(90, voided.declaredMinutes)
        assertEquals(75, voided.heldMinutes)
        assertThrows<IllegalStateException> { m.complete(t0.plus(Duration.ofMinutes(90))) }
    }

    // ---- Checkpoint ----

    @Test
    fun `checkpoint prompt states the cost in the coach voice`() {
        assertEquals("Proceeding costs you. Mind −4.", Checkpoint.prompt(com.forge.core.model.Pillar.MIND))
    }
}
