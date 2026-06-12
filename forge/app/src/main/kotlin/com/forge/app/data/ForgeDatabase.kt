package com.forge.app.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Local-first store. Hard architecture rule #3: every row here is shaped so
 * any future sync can be E2E-encrypted — no server-side interpretation is
 * ever needed, so ciphertext sync Just Works later.
 *
 * Rows mirror the core ledger: categories, outcomes, timestamps. No
 * package names, no URLs, no content.
 */
@Entity(tableName = "ledger")
data class LedgerRow(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val atEpochMs: Long,
    /** LedgerEntry subtype discriminator, e.g. "CHECKPOINT", "PROMISE_KEPT". */
    val kind: String,
    /** JSON payload of the core LedgerEntry (events-not-content by type). */
    val payload: String,
)

@Entity(tableName = "day_scores")
data class DayScoreRow(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val epochDay: Long,
    val pillar: String,
    val dayScore: Double,
)

@Entity(tableName = "unlocks")
data class UnlockRow(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val atEpochMs: Long,
)

@Entity(tableName = "category_overrides")
data class CategoryOverrideRow(
    @PrimaryKey val packageName: String,
    val category: String,
)

@Dao
interface ForgeDao {
    @Insert
    suspend fun insertLedger(row: LedgerRow)

    @Query("SELECT * FROM ledger WHERE atEpochMs >= :fromMs ORDER BY atEpochMs DESC")
    suspend fun ledgerSince(fromMs: Long): List<LedgerRow>

    @Insert
    suspend fun insertDayScore(row: DayScoreRow)

    @Query("SELECT * FROM day_scores WHERE pillar = :pillar ORDER BY epochDay DESC LIMIT :days")
    suspend fun recentDayScores(pillar: String, days: Int): List<DayScoreRow>

    @Query("SELECT * FROM day_scores WHERE pillar = :pillar AND epochDay = :epochDay")
    suspend fun dayScoreOn(pillar: String, epochDay: Long): DayScoreRow?

    @Insert
    suspend fun insertUnlock(row: UnlockRow)

    @Query("SELECT * FROM unlocks WHERE atEpochMs BETWEEN :fromMs AND :toMs")
    suspend fun unlocksBetween(fromMs: Long, toMs: Long): List<UnlockRow>

    @Query("SELECT * FROM category_overrides")
    suspend fun categoryOverrides(): List<CategoryOverrideRow>
}

@Database(
    entities = [LedgerRow::class, DayScoreRow::class, UnlockRow::class, CategoryOverrideRow::class],
    version = 1,
    exportSchema = true,
)
abstract class ForgeDatabase : RoomDatabase() {
    abstract fun dao(): ForgeDao

    companion object {
        @Volatile private var instance: ForgeDatabase? = null

        fun get(context: Context): ForgeDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext, ForgeDatabase::class.java, "forge.db",
                ).build().also { instance = it }
            }
    }
}
