package com.forge.core.interventions

import com.forge.core.ledger.LedgerEntry
import java.time.Duration
import java.time.Instant

/**
 * Monolith: a hardware-enforced focus contract. Declare a duration,
 * distraction apps lock, leaving early voids the session and logs a
 * broken promise. Completing it is verified deep work.
 */
class MonolithSession(
    val declaredMinutes: Long,
    val startedAt: Instant,
) {
    init {
        require(declaredMinutes > 0) { "declare a positive duration" }
    }

    enum class Status { ACTIVE, VERIFIED, VOIDED }

    var status: Status = Status.ACTIVE
        private set

    val endsAt: Instant get() = startedAt.plus(Duration.ofMinutes(declaredMinutes))

    /** Returns null until the declared duration has actually elapsed. */
    fun complete(now: Instant): LedgerEntry.MonolithVerified? {
        check(status == Status.ACTIVE) { "session already settled" }
        if (now.isBefore(endsAt)) return null
        status = Status.VERIFIED
        return LedgerEntry.MonolithVerified(now, declaredMinutes)
    }

    /** Leaving early. The session is void — partial time earns nothing. */
    fun abandon(now: Instant): LedgerEntry.MonolithVoided {
        check(status == Status.ACTIVE) { "session already settled" }
        status = Status.VOIDED
        val held = Duration.between(startedAt, now).toMinutes().coerceAtLeast(0)
        return LedgerEntry.MonolithVoided(now, declaredMinutes, held)
    }
}
