package com.forge.core.signals

import com.forge.core.model.AppCategory
import com.forge.core.model.AppSession
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ImpulseDetectorTest {

    private val zone = ZoneId.of("Asia/Dhaka")
    private val day = LocalDate.of(2026, 6, 12)

    private fun at(h: Int, m: Int): Instant = day.atTime(h, m).atZone(zone).toInstant()

    private fun drain(startH: Int, startM: Int, minutes: Long) =
        AppSession(AppCategory.DRAIN, at(startH, startM), at(startH, startM).plusSeconds(minutes * 60))

    @Test
    fun `late night drain session is flagged`() {
        val signals = ImpulseDetector.detect(listOf(drain(1, 12, 35)), zone)
        assertEquals(1, signals.size)
        assertTrue(signals[0] is ImpulseSignal.LateNightSession)
    }

    @Test
    fun `same session in the afternoon is not flagged`() {
        val signals = ImpulseDetector.detect(listOf(drain(15, 0, 35)), zone)
        assertTrue(signals.isEmpty())
    }

    @Test
    fun `rapid switching loop is flagged once per cluster`() {
        val sessions = (0 until 7).map { i ->
            AppSession(AppCategory.DRAIN, at(14, 0).plusSeconds(i * 30L), at(14, 0).plusSeconds(i * 30L + 20))
        }
        val signals = ImpulseDetector.detect(sessions, zone)
        val loops = signals.filterIsInstance<ImpulseSignal.RapidSwitchLoop>()
        assertEquals(1, loops.size)
        assertEquals(7, loops[0].switches)
    }

    @Test
    fun `binge burst merges near-contiguous sessions`() {
        val sessions = listOf(
            drain(20, 0, 25),
            drain(20, 26, 30), // 1-minute gap: same binge
        )
        val signals = ImpulseDetector.detect(sessions, zone)
        val binges = signals.filterIsInstance<ImpulseSignal.BingeBurst>()
        assertEquals(1, binges.size)
        assertEquals(56, binges[0].minutes)
    }

    @Test
    fun `growth and neutral sessions never trigger impulse signals`() {
        val sessions = listOf(
            AppSession(AppCategory.GROWTH, at(1, 0), at(1, 0).plusSeconds(3600)),
            AppSession(AppCategory.NEUTRAL, at(2, 0), at(2, 0).plusSeconds(3600)),
        )
        assertTrue(ImpulseDetector.detect(sessions, zone).isEmpty())
    }

    @Test
    fun `scattered short sessions are clean`() {
        val sessions = listOf(drain(9, 0, 5), drain(13, 0, 8), drain(18, 0, 10))
        assertTrue(ImpulseDetector.detect(sessions, zone).isEmpty())
    }
}
