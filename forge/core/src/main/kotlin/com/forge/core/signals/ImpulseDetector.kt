package com.forge.core.signals

import com.forge.core.model.AppCategory
import com.forge.core.model.AppSession
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

/**
 * Impulse Detection: pattern recognition over usage *events* —
 * never content reading. The engine sees timestamps and categories only.
 */
sealed interface ImpulseSignal {
    val at: Instant

    /** Drain session of meaningful length starting in the dead of night. */
    data class LateNightSession(override val at: Instant, val minutes: Long) : ImpulseSignal

    /** Compulsive loop: many drain-app opens packed into a few minutes. */
    data class RapidSwitchLoop(override val at: Instant, val switches: Int) : ImpulseSignal

    /** One long contiguous drain binge. */
    data class BingeBurst(override val at: Instant, val minutes: Long) : ImpulseSignal
}

object ImpulseDetector {

    private val LATE_NIGHT_START: LocalTime = LocalTime.MIDNIGHT
    private val LATE_NIGHT_END: LocalTime = LocalTime.of(5, 0)
    private const val LATE_NIGHT_MIN_MINUTES = 20L

    private const val SWITCH_WINDOW_MINUTES = 5L
    private const val SWITCH_THRESHOLD = 6

    private const val BINGE_MINUTES = 45L

    /** Gap below which consecutive drain sessions are one continuous binge. */
    private const val MERGE_GAP_MINUTES = 2L

    fun detect(sessions: List<AppSession>, zone: ZoneId): List<ImpulseSignal> {
        val drains = sessions.filter { it.category == AppCategory.DRAIN }.sortedBy { it.start }
        val signals = mutableListOf<ImpulseSignal>()

        // Late-night solo sessions.
        for (s in drains) {
            val t = s.start.atZone(zone).toLocalTime()
            val lateNight = t >= LATE_NIGHT_START && t < LATE_NIGHT_END
            if (lateNight && s.minutes >= LATE_NIGHT_MIN_MINUTES) {
                signals += ImpulseSignal.LateNightSession(s.start, s.minutes)
            }
        }

        // Rapid app-switching loops: session starts clustered in a short window.
        var i = 0
        while (i < drains.size) {
            val windowEnd = drains[i].start.plus(Duration.ofMinutes(SWITCH_WINDOW_MINUTES))
            var j = i
            while (j < drains.size && !drains[j].start.isAfter(windowEnd)) j++
            val count = j - i
            if (count >= SWITCH_THRESHOLD) {
                signals += ImpulseSignal.RapidSwitchLoop(drains[i].start, count)
                i = j // don't double-report the same cluster
            } else {
                i++
            }
        }

        // Binge bursts: merge near-contiguous drain time, flag long runs.
        var runStart: Instant? = null
        var runEnd: Instant? = null
        fun flush() {
            val s = runStart ?: return
            val e = runEnd ?: return
            val mins = Duration.between(s, e).toMinutes()
            if (mins >= BINGE_MINUTES) signals += ImpulseSignal.BingeBurst(s, mins)
        }
        for (s in drains) {
            val end = runEnd
            if (end == null || Duration.between(end, s.start).toMinutes() > MERGE_GAP_MINUTES) {
                flush()
                runStart = s.start
                runEnd = s.end
            } else if (s.end.isAfter(end)) {
                runEnd = s.end
            }
        }
        flush()

        return signals.sortedBy { it.at }
    }
}
