package com.forge.core.contracts

import com.forge.core.ledger.ExcuseTag
import com.forge.core.ledger.LedgerEntry
import com.forge.core.ledger.VerificationSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.time.LocalDate
import kotlin.test.assertEquals

class DailyContractTest {

    private val t0: Instant = Instant.parse("2026-06-12T07:00:00Z")
    private val contract = DailyContract(
        LocalDate.of(2026, 6, 12),
        listOf(
            ContractItem("p1", "Gym at 7pm", VerificationSource.GPS_GYM),
            ContractItem("p2", "90-minute Monolith", VerificationSource.MONOLITH),
        ),
    )

    @Test
    fun `confirming converts proposals into promises`() {
        val promises = contract.confirm(t0)
        assertEquals(2, promises.size)
        assertEquals("p1", promises[0].promiseId)
        assertEquals(VerificationSource.MONOLITH, promises[1].verification)
    }

    @Test
    fun `unconfirmed contracts cannot be settled`() {
        assertThrows<IllegalStateException> { contract.markKept("p1", t0) }
    }

    @Test
    fun `settled promises cannot flip`() {
        contract.confirm(t0)
        contract.markKept("p1", t0.plusSeconds(3600))
        assertThrows<IllegalStateException> { contract.markBroken("p1", t0.plusSeconds(7200)) }
        assertEquals(1, contract.keptCount)
        assertEquals(2, contract.totalCount)
    }

    @Test
    fun `a broken promise can carry its excuse as evidence`() {
        contract.confirm(t0)
        val excuse = LedgerEntry.ExcuseLogged(t0.plusSeconds(3600), ExcuseTag.TIRED, "exhausted")
        val broken = contract.markBroken("p2", t0.plusSeconds(3600), excuse)
        assertEquals(ExcuseTag.TIRED, broken.excuse?.tag)
    }

    @Test
    fun `excuse tagging maps stated text`() {
        assertEquals(ExcuseTag.TIRED, ExcuseTag.fromText("Skipping today, exhausted"))
        assertEquals(ExcuseTag.BUSY, ExcuseTag.fromText("no time, exam tomorrow"))
        assertEquals(ExcuseTag.OTHER, ExcuseTag.fromText("mercury is in retrograde"))
    }
}
