package com.forge.core.signals

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SleepWindowTest {

    private val zone = ZoneId.of("Asia/Dhaka")
    private val morning = LocalDate.of(2026, 6, 12)
    private val target = SleepTarget(bedtime = LocalTime.of(22, 30), wake = LocalTime.of(6, 0))

    private fun at(date: LocalDate, h: Int, m: Int) =
        date.atTime(h, m).atZone(zone).toInstant()

    @Test
    fun `window derived from last night unlock and first morning unlock`() {
        val unlocks = listOf(
            at(morning.minusDays(1), 21, 0),
            at(morning.minusDays(1), 23, 10), // last touch before sleep
            at(morning, 6, 45),               // first touch after waking
            at(morning, 9, 0),
        )
        val w = Sleep.deriveWindow(unlocks, zone, morning)
        assertNotNull(w)
        assertEquals(at(morning.minusDays(1), 23, 10), w.lastUnlock)
        assertEquals(at(morning, 6, 45), w.firstUnlock)
        assertEquals(7 * 60 + 35, w.duration.toMinutes().toInt())
    }

    @Test
    fun `no night activity means no window`() {
        val unlocks = listOf(at(morning, 10, 0))
        assertNull(Sleep.deriveWindow(unlocks, zone, morning))
    }

    @Test
    fun `on-target night scores full adherence`() {
        val w = SleepWindow(at(morning.minusDays(1), 22, 15), at(morning, 6, 10))
        assertEquals(1.0, Sleep.adherence(w, target, zone), 1e-9)
    }

    @Test
    fun `phone past 1am wrecks the bed component`() {
        val lateNight = SleepWindow(at(morning, 1, 30), at(morning, 9, 0))
        val onTime = SleepWindow(at(morning.minusDays(1), 22, 15), at(morning, 6, 10))
        val late = Sleep.adherence(lateNight, target, zone)
        assertTrue(late < Sleep.adherence(onTime, target, zone))
        assertEquals(0.4, late, 1e-9) // full duration credit, zero bed credit
    }

    @Test
    fun `short night loses duration credit proportionally`() {
        val shortNight = SleepWindow(at(morning.minusDays(1), 22, 0), at(morning, 1, 45))
        val a = Sleep.adherence(shortNight, target, zone)
        assertEquals(0.6 + 0.4 * (225.0 / 450.0), a, 1e-9)
    }
}
