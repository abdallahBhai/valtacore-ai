package com.forge.core.ledger

import com.forge.core.model.AppCategory
import com.forge.core.model.Pillar
import java.time.Instant

enum class CheckpointOutcome { DEFLECTED, PROCEEDED }

/** What verified a promise. SELF_REPORT exists but is weighted low. */
enum class VerificationSource {
    MONOLITH, USAGE_ENGINE, SLEEP_WINDOW, STEPS, GPS_GYM, CV_WORKOUT, SELF_REPORT,
}

/**
 * Excuses are first-class data. The tag is what the Tribunal correlates;
 * the raw stated text is user-authored and may be kept verbatim.
 */
enum class ExcuseTag {
    TIRED, BUSY, SICK, UNMOTIVATED, SOCIAL, TRAVEL, OTHER;

    companion object {
        fun fromText(text: String): ExcuseTag {
            val t = text.lowercase()
            return when {
                listOf("tired", "exhaust", "sleep", "drained", "fatigue").any { it in t } -> TIRED
                listOf("busy", "no time", "work", "deadline", "class", "exam").any { it in t } -> BUSY
                listOf("sick", "ill", "fever", "injur", "pain", "hurt").any { it in t } -> SICK
                listOf("lazy", "motivat", "feel like", "mood", "can't be bothered").any { it in t } -> UNMOTIVATED
                listOf("friend", "family", "party", "wedding", "guest").any { it in t } -> SOCIAL
                listOf("travel", "flight", "trip", "moving", "on the road").any { it in t } -> TRAVEL
                else -> OTHER
            }
        }
    }
}

/**
 * FORGE's memory. Every promise, excuse, checkpoint event, and verdict —
 * evidence, not notes. Hard rule: entries carry categories, outcomes, and
 * timestamps. Never URLs, never site names, never surveilled content.
 */
sealed interface LedgerEntry {
    val at: Instant

    data class CheckpointEvent(
        override val at: Instant,
        val category: AppCategory,
        val outcome: CheckpointOutcome,
        val pillar: Pillar,
        val costApplied: Int,
    ) : LedgerEntry

    data class PromiseMade(
        override val at: Instant,
        val promiseId: String,
        val description: String,
        val verification: VerificationSource,
    ) : LedgerEntry

    data class PromiseKept(
        override val at: Instant,
        val promiseId: String,
        val verification: VerificationSource,
    ) : LedgerEntry

    data class PromiseBroken(
        override val at: Instant,
        val promiseId: String,
        val excuse: ExcuseLogged? = null,
    ) : LedgerEntry

    data class ExcuseLogged(
        override val at: Instant,
        val tag: ExcuseTag,
        val statedText: String,
    ) : LedgerEntry

    data class IronCommitted(
        override val at: Instant,
        val until: Instant,
    ) : LedgerEntry

    data class IronEarlyExit(
        override val at: Instant,
        val scoreCost: Int,
    ) : LedgerEntry

    data class MonolithVerified(
        override val at: Instant,
        val minutes: Long,
    ) : LedgerEntry

    data class MonolithVoided(
        override val at: Instant,
        val declaredMinutes: Long,
        val heldMinutes: Long,
    ) : LedgerEntry

    data class TribunalIssued(
        override val at: Instant,
        val weekStartEpochDay: Long,
    ) : LedgerEntry
}
