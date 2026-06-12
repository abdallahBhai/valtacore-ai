package com.forge.core.contracts

import com.forge.core.ledger.LedgerEntry
import com.forge.core.ledger.VerificationSource
import java.time.Instant
import java.time.LocalDate

/**
 * Daily contract: AI proposes, the user confirms in the morning brief,
 * the engine verifies. Plan → enforce → verify → score.
 */
data class ContractItem(
    val id: String,
    val description: String,
    val verification: VerificationSource,
)

class DailyContract(
    val date: LocalDate,
    proposedItems: List<ContractItem>,
) {
    enum class ItemStatus { PENDING, KEPT, BROKEN }

    var confirmed: Boolean = false
        private set

    private val statuses = proposedItems.associate { it.id to ItemStatus.PENDING }.toMutableMap()
    val items: List<ContractItem> = proposedItems

    /** Morning brief: confirming converts proposals into promises. */
    fun confirm(at: Instant): List<LedgerEntry.PromiseMade> {
        check(!confirmed) { "contract already confirmed" }
        confirmed = true
        return items.map { LedgerEntry.PromiseMade(at, it.id, it.description, it.verification) }
    }

    fun markKept(id: String, at: Instant): LedgerEntry.PromiseKept {
        val item = settle(id, ItemStatus.KEPT)
        return LedgerEntry.PromiseKept(at, id, item.verification)
    }

    fun markBroken(id: String, at: Instant, excuse: LedgerEntry.ExcuseLogged? = null): LedgerEntry.PromiseBroken {
        settle(id, ItemStatus.BROKEN)
        return LedgerEntry.PromiseBroken(at, id, excuse)
    }

    private fun settle(id: String, to: ItemStatus): ContractItem {
        check(confirmed) { "contract not confirmed" }
        val item = items.find { it.id == id } ?: error("unknown promise: $id")
        check(statuses[id] == ItemStatus.PENDING) { "promise already settled: $id" }
        statuses[id] = to
        return item
    }

    fun statusOf(id: String): ItemStatus = statuses[id] ?: error("unknown promise: $id")
    val keptCount: Int get() = statuses.values.count { it == ItemStatus.KEPT }
    val totalCount: Int get() = if (confirmed) items.size else 0
}
