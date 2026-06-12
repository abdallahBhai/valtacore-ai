package com.forge.core.retention

import java.time.Duration
import java.time.Instant

/**
 * Comeback Protocol: structured 72-hour relapse recovery, built for the
 * exact moment habit apps lose users — the day after failure.
 * Scores freeze, three small mandatory wins, then normal scoring resumes.
 */
class ComebackProtocol {

    companion object {
        val WINDOW: Duration = Duration.ofHours(72)
        const val REQUIRED_WINS = 3
    }

    data class Win(val description: String, val at: Instant)

    sealed interface State {
        data object Inactive : State
        data class Active(
            val startedAt: Instant,
            val deadline: Instant,
            val wins: List<Win>,
        ) : State

        data class Completed(val at: Instant) : State
        data class Expired(val at: Instant) : State
    }

    var state: State = State.Inactive
        private set

    /** While active, momentum scores are frozen — no digging deeper. */
    val scoresFrozen: Boolean get() = state is State.Active

    fun trigger(now: Instant) {
        check(state !is State.Active) { "comeback already running" }
        state = State.Active(now, now.plus(WINDOW), emptyList())
    }

    /** Returns true when this win completes the protocol. */
    fun recordWin(win: Win): Boolean {
        val s = state
        check(s is State.Active) { "comeback not active" }
        if (win.at.isAfter(s.deadline)) {
            state = State.Expired(win.at)
            return false
        }
        val wins = s.wins + win
        return if (wins.size >= REQUIRED_WINS) {
            state = State.Completed(win.at)
            true
        } else {
            state = s.copy(wins = wins)
            false
        }
    }

    /** Call on any clock tick; expires the window if it lapsed. */
    fun checkExpiry(now: Instant) {
        val s = state
        if (s is State.Active && now.isAfter(s.deadline)) {
            state = State.Expired(now)
        }
    }
}
