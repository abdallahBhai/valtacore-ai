package com.forge.core.persona

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DistressGuardTest {

    @Test
    fun `crisis language drops the persona instantly`() {
        listOf(
            "I want to die",
            "what's the point, I'd be better off dead",
            "been thinking about suicide",
            "I keep hurting myself",
        ).forEach { input ->
            assertEquals(DistressGuard.Level.CRISIS, DistressGuard.screen(input), "missed: $input")
        }
    }

    @Test
    fun `ordinary gym-bro hyperbole does not trigger`() {
        listOf(
            "that workout killed me",
            "I'm dead tired after leg day",
            "skipping today, exhausted",
            "this deadline is murdering my schedule",
        ).forEach { input ->
            assertEquals(DistressGuard.Level.NONE, DistressGuard.screen(input), "false positive: $input")
        }
    }

    @Test
    fun `support message has no persona and points to humans`() {
        val msg = DistressGuard.supportMessage()
        assertTrue("findahelpline.com" in msg)
        assertTrue("988" in msg)
        assertTrue("score" !in msg.lowercase().substringAfter("\n")) // no score talk after the opener
    }
}
