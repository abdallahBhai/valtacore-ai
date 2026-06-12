package com.forge.core.interventions

import com.forge.core.ledger.CheckpointOutcome
import com.forge.core.ledger.LedgerEntry
import com.forge.core.model.AppCategory
import com.forge.core.model.Pillar
import java.time.Instant

/**
 * Checkpoint mode: the default friction layer. An overlay states the cost
 * ("Proceeding costs you. Mind –4."), optionally makes the user wait ten
 * seconds, and logs the outcome either way. Deflections are visible progress.
 */
object Checkpoint {
    const val PROCEED_COST = 4
    const val WAIT_SECONDS = 10

    fun prompt(pillar: Pillar): String =
        "Proceeding costs you. ${pillar.name.lowercase().replaceFirstChar { it.uppercase() }} −$PROCEED_COST."

    fun record(
        at: Instant,
        category: AppCategory,
        pillar: Pillar,
        outcome: CheckpointOutcome,
    ): LedgerEntry.CheckpointEvent = LedgerEntry.CheckpointEvent(
        at = at,
        category = category,
        outcome = outcome,
        pillar = pillar,
        costApplied = if (outcome == CheckpointOutcome.PROCEEDED) PROCEED_COST else 0,
    )
}
