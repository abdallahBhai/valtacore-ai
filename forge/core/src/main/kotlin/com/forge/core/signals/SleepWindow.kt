package com.forge.core.signals

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/** User's declared sleep target. No wearable needed. */
data class SleepTarget(
    val bedtime: LocalTime,
    val wake: LocalTime,
) {
    val targetDuration: Duration
        get() {
            val mins = Duration.between(bedtime, wake).toMinutes().let {
                if (it <= 0) it + 24 * 60 else it
            }
            return Duration.ofMinutes(mins)
        }
}

/** Derived purely from unlock timestamps: last unlock at night, first in the morning. */
data class SleepWindow(val lastUnlock: Instant, val firstUnlock: Instant) {
    val duration: Duration get() = Duration.between(lastUnlock, firstUnlock)
}

object Sleep {

    /** Night is considered to start at 20:00 and end at 05:00 next day. */
    private val NIGHT_START: LocalTime = LocalTime.of(20, 0)
    private val NIGHT_END: LocalTime = LocalTime.of(5, 0)

    /**
     * Derive the sleep window for the night leading into [morningOf] from raw
     * unlock timestamps. Returns null if the night has no usable signal
     * (e.g. phone untouched all evening — counts as perfect elsewhere).
     */
    fun deriveWindow(unlocks: List<Instant>, zone: ZoneId, morningOf: LocalDate): SleepWindow? {
        val nightStart = morningOf.minusDays(1).atTime(NIGHT_START).atZone(zone).toInstant()
        val nightEnd = morningOf.atTime(NIGHT_END).atZone(zone).toInstant()
        val noon = morningOf.atTime(LocalTime.NOON).atZone(zone).toInstant()

        val lastNight = unlocks.filter { it >= nightStart && it <= nightEnd }.maxOrNull() ?: return null
        val firstMorning = unlocks.filter { it > lastNight && it <= noon }.minOrNull() ?: return null
        return SleepWindow(lastNight, firstMorning)
    }

    /**
     * 0..1 adherence: 60% for putting the phone down on time
     * (linear penalty, two hours late = zero) and 40% for getting
     * the target amount of time inside the window.
     */
    fun adherence(window: SleepWindow, target: SleepTarget, zone: ZoneId): Double {
        val noon = LocalTime.NOON
        val bedLocal = window.lastUnlock.atZone(zone).toLocalDateTime()
        val date = bedLocal.toLocalDate()
        // Anchor the bedtime target to the night the unlock belongs to:
        // a 01:30 unlock is measured against *yesterday's* 22:30 target.
        val targetBed = if (target.bedtime >= noon) {
            if (bedLocal.toLocalTime() < noon) date.minusDays(1).atTime(target.bedtime)
            else date.atTime(target.bedtime)
        } else {
            if (bedLocal.toLocalTime() >= noon) date.plusDays(1).atTime(target.bedtime)
            else date.atTime(target.bedtime)
        }

        val minutesLate = Duration.between(targetBed, bedLocal).toMinutes().coerceAtLeast(0)
        val bedComponent = (1.0 - minutesLate / 120.0).coerceIn(0.0, 1.0) * 0.6

        val ratio = window.duration.toMinutes().toDouble() / target.targetDuration.toMinutes()
        val durationComponent = ratio.coerceIn(0.0, 1.0) * 0.4

        return bedComponent + durationComponent
    }
}
