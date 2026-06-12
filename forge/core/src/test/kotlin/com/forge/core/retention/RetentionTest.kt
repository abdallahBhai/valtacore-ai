package com.forge.core.retention

import com.forge.core.model.Pillar
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RetentionTest {

    private val t0: Instant = Instant.parse("2026-06-12T08:00:00Z")

    // ---- Shadow ----

    @Test
    fun `shadow comparison is per pillar with an overall verdict`() {
        val result = Shadow.compare(
            today = mapOf(Pillar.MIND to 80.0, Pillar.PRODUCTIVITY to 60.0, Pillar.HEALTH to 70.0),
            shadowScores = mapOf(Pillar.MIND to 70.0, Pillar.PRODUCTIVITY to 65.0, Pillar.HEALTH to 50.0),
        )
        assertEquals(3, result.comparisons.size)
        assertEquals(Shadow.Verdict.AHEAD, result.verdict)
        assertEquals(10.0, result.comparisons.first { it.pillar == Pillar.MIND }.delta, 1e-9)
    }

    @Test
    fun `pillars without 30-day history are not contested`() {
        val result = Shadow.compare(
            today = mapOf(Pillar.MIND to 80.0, Pillar.HEALTH to 40.0),
            shadowScores = mapOf(Pillar.HEALTH to 60.0),
        )
        assertEquals(1, result.comparisons.size)
        assertEquals(Shadow.Verdict.BEHIND, result.verdict)
    }

    // ---- Comeback Protocol ----

    @Test
    fun `comeback freezes scores until three wins inside 72 hours`() {
        val cb = ComebackProtocol()
        assertFalse(cb.scoresFrozen)

        cb.trigger(t0)
        assertTrue(cb.scoresFrozen)

        assertFalse(cb.recordWin(ComebackProtocol.Win("slept on time", t0.plus(Duration.ofHours(10)))))
        assertFalse(cb.recordWin(ComebackProtocol.Win("one monolith", t0.plus(Duration.ofHours(26)))))
        assertTrue(cb.scoresFrozen)

        val done = cb.recordWin(ComebackProtocol.Win("walked 8k steps", t0.plus(Duration.ofHours(40))))
        assertTrue(done)
        assertFalse(cb.scoresFrozen)
        assertTrue(cb.state is ComebackProtocol.State.Completed)
    }

    @Test
    fun `missing the 72 hour window expires the protocol`() {
        val cb = ComebackProtocol()
        cb.trigger(t0)
        cb.recordWin(ComebackProtocol.Win("one win", t0.plus(Duration.ofHours(5))))
        cb.checkExpiry(t0.plus(Duration.ofHours(73)))
        assertTrue(cb.state is ComebackProtocol.State.Expired)
        assertFalse(cb.scoresFrozen)
    }
}
