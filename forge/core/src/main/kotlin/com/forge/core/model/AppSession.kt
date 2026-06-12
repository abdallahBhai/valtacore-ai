package com.forge.core.model

import java.time.Duration
import java.time.Instant

/**
 * One continuous foreground stretch of an app, reduced to its category.
 * The package name stays on-device in the collector layer; the engine
 * only ever sees the category.
 */
data class AppSession(
    val category: AppCategory,
    val start: Instant,
    val end: Instant,
) {
    init {
        require(!end.isBefore(start)) { "session end before start" }
    }

    val duration: Duration get() = Duration.between(start, end)
    val minutes: Long get() = duration.toMinutes()
}
