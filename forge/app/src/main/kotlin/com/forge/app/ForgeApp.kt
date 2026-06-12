package com.forge.app

import android.app.Application
import android.content.Context
import com.forge.app.collect.UsageStatsCollector
import com.forge.app.data.ForgeDatabase
import com.forge.app.data.UnlockRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Instant

class ForgeApp : Application() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: ForgeDatabase by lazy { ForgeDatabase.get(this) }
    val scores: ScoreRepository by lazy {
        ScoreRepository(database.dao(), UsageStatsCollector(this))
    }

    fun recordUnlock(at: Instant) {
        scope.launch { database.dao().insertUnlock(UnlockRow(atEpochMs = at.toEpochMilli())) }
    }

    companion object {
        fun from(context: Context): ForgeApp = context.applicationContext as ForgeApp
    }
}
