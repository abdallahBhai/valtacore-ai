package com.forge.app.collect

import com.forge.core.model.AppCategory

/**
 * Package → category mapping. This map is local configuration: the engine
 * consumes only the resulting category, and package names never appear in
 * the ledger. User overrides are stored in Room and merged on top.
 */
object AppCategorizer {

    private val defaults: Map<String, AppCategory> = buildMap {
        // GROWTH — audio/reading/learning
        for (p in listOf(
            "com.audible.application", "com.amazon.kindle", "com.google.android.apps.books",
            "com.spotify.music.podcast", "fm.castbox.audiobook.radio.podcast",
            "org.coursera.android", "com.duolingo", "com.headway.books",
        )) put(p, AppCategory.GROWTH)

        // DRAIN — doomscroll surfaces
        for (p in listOf(
            "com.instagram.android", "com.zhiliaoapp.musically", "com.ss.android.ugc.trill",
            "com.twitter.android", "com.facebook.katana", "com.snapchat.android",
            "com.reddit.frontpage", "tv.twitch.android.app", "com.netflix.mediaclient",
        )) put(p, AppCategory.DRAIN)

        // TOOL — protected during Monolith
        for (p in listOf(
            "com.google.android.apps.docs", "com.microsoft.office.word", "com.notion.id",
            "md.obsidian", "com.todoist", "com.google.android.calendar",
        )) put(p, AppCategory.TOOL)
    }

    /** YouTube is contested ground; default DRAIN, user can recategorize. */
    fun categorize(packageName: String, overrides: Map<String, AppCategory> = emptyMap()): AppCategory =
        overrides[packageName]
            ?: defaults[packageName]
            ?: if (packageName == "com.google.android.youtube") AppCategory.DRAIN
            else AppCategory.NEUTRAL
}
