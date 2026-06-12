package com.forge.core.model

/**
 * Apps are scored by category only. The engine never logs which app,
 * which site, or what was viewed — events, never content.
 */
enum class AppCategory {
    /** Audio/reading/learning apps. Counts toward Mind, capped. */
    GROWTH,

    /** Doomscroll surfaces. Lowers Productivity; subject to Checkpoint/Iron. */
    DRAIN,

    /** Messaging, maps, banking — ignored by scoring. */
    NEUTRAL,

    /** Work tools — protected during Monolith sessions. */
    TOOL,
}
