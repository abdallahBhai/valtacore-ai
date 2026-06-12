package com.forge.core.interventions

import com.forge.core.ledger.LedgerEntry
import com.forge.core.model.AppCategory
import java.time.Duration
import java.time.Instant

/**
 * Iron mode: a hard block entered as a commitment with a duration.
 * Early exit costs score, is logged, and only takes effect after a
 * 24-hour cooldown — the urge passes before the unlock does.
 */
class IronMode(val earlyExitCost: Int = 10) {

    companion object {
        val COOLDOWN: Duration = Duration.ofHours(24)
    }

    sealed interface State {
        data object Idle : State

        data class Active(
            val committedAt: Instant,
            val until: Instant,
            val restricted: Set<AppCategory>,
        ) : State

        data class ExitRequested(
            val active: Active,
            val requestedAt: Instant,
            val exitEffectiveAt: Instant,
        ) : State
    }

    var state: State = State.Idle
        private set

    fun commit(now: Instant, duration: Duration, restricted: Set<AppCategory>): LedgerEntry.IronCommitted {
        check(state is State.Idle) { "Iron commitment already active" }
        require(!duration.isNegative && !duration.isZero) { "duration must be positive" }
        val until = now.plus(duration)
        state = State.Active(now, until, restricted)
        return LedgerEntry.IronCommitted(now, until)
    }

    /** Starts the 24h cooldown. Nothing unlocks yet. */
    fun requestEarlyExit(now: Instant): Instant {
        val s = state
        check(s is State.Active) { "no active Iron commitment" }
        val effective = now.plus(COOLDOWN)
        state = State.ExitRequested(s, now, effective)
        return effective
    }

    /** The urge survived the cooldown. Unlock, charge the cost, log the break. */
    fun confirmEarlyExit(now: Instant): LedgerEntry.IronEarlyExit {
        val s = state
        check(s is State.ExitRequested) { "no exit requested" }
        check(!now.isBefore(s.exitEffectiveAt)) {
            "cooldown not over: exit effective at ${s.exitEffectiveAt}"
        }
        state = State.Idle
        return LedgerEntry.IronEarlyExit(now, earlyExitCost)
    }

    /** Thought better of it during cooldown. The commitment stands. */
    fun cancelExitRequest() {
        val s = state
        check(s is State.ExitRequested) { "no exit requested" }
        state = s.active
    }

    fun isBlocking(category: AppCategory, now: Instant): Boolean {
        val active = when (val s = state) {
            is State.Active -> s
            is State.ExitRequested -> s.active
            State.Idle -> return false
        }
        if (now.isAfter(active.until)) {
            state = State.Idle // commitment served in full
            return false
        }
        return category in active.restricted
    }
}
