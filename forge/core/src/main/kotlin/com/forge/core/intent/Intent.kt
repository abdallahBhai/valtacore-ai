package com.forge.core.intent

import com.forge.core.ledger.ExcuseTag

/**
 * The input engine's contract: one entry point for everything, parsed by
 * a stateless LLM function-calling pass into one of these structured
 * actions. No open chat, no conversation memory. Parse → act → terse
 * confirmation.
 *
 * These types are the single source of truth for the router's tool
 * schema; the Android layer generates the Anthropic tool definitions
 * from them.
 */
sealed interface Intent {

    // ---- log ----
    data class LogActivity(val description: String, val quantity: Int?, val unit: String?) : Intent
    data class LogTaskDone(val description: String) : Intent
    data class LogExcuse(val tag: ExcuseTag, val statedText: String) : Intent

    // ---- mutate ----
    data class RescheduleItem(val itemDescription: String, val newTime: String) : Intent
    data class DeferItem(val itemDescription: String, val reason: String?) : Intent
    data class SkipToday(val reason: String?) : Intent

    // ---- declare ----
    data class DeclareGoal(val goalText: String) : Intent
    data class StartMonolith(val minutes: Long) : Intent
    data class CommitIron(val hours: Long) : Intent
    data class RequestIronExit(val reason: String?) : Intent
    data class GymCheckIn(val confirm: Boolean) : Intent
    data class StartWorkout(val exercise: String) : Intent

    // ---- query (bounded: about the user's own data only) ----
    data class QueryScore(val pillar: String?) : Intent
    data object QueryTodayContract : Intent
    data class QueryLedger(val days: Int) : Intent

    /** Router couldn't map the utterance to an action. */
    data class Unrecognized(val raw: String) : Intent
}
