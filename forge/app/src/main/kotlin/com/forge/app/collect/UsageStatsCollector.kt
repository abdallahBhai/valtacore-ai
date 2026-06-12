package com.forge.app.collect

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.forge.core.model.AppCategory
import com.forge.core.model.AppSession
import java.time.Instant

/**
 * Reduces raw UsageEvents to categorized [AppSession]s. Package names die
 * here — everything downstream sees categories and timestamps only.
 */
class UsageStatsCollector(
    private val context: Context,
    private val overrides: () -> Map<String, AppCategory> = { emptyMap() },
) {

    fun sessionsBetween(from: Instant, to: Instant): List<AppSession> {
        val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val events = usm.queryEvents(from.toEpochMilli(), to.toEpochMilli())

        val sessions = mutableListOf<AppSession>()
        val openSince = mutableMapOf<String, Long>()
        val event = UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            when (event.eventType) {
                UsageEvents.Event.ACTIVITY_RESUMED ->
                    openSince[event.packageName] = event.timeStamp

                UsageEvents.Event.ACTIVITY_PAUSED,
                UsageEvents.Event.ACTIVITY_STOPPED -> {
                    val start = openSince.remove(event.packageName) ?: continue
                    if (event.timeStamp > start) {
                        sessions += AppSession(
                            category = AppCategorizer.categorize(event.packageName, overrides()),
                            start = Instant.ofEpochMilli(start),
                            end = Instant.ofEpochMilli(event.timeStamp),
                        )
                    }
                }
            }
        }
        return sessions.sortedBy { it.start }
    }
}
