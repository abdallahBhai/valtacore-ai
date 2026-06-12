package com.forge.core.persona

/**
 * Hard rule, built day one: if user input signals genuine crisis, the
 * drill-sergeant persona drops instantly and the app points to real
 * human support. Every output path must pass through this guard first.
 *
 * The keyword screen errs toward triggering: a false positive costs one
 * soft message; a false negative is unacceptable.
 */
object DistressGuard {

    enum class Level { NONE, CRISIS }

    private val CRISIS_MARKERS = listOf(
        "kill myself", "killing myself", "suicide", "suicidal",
        "end my life", "ending my life", "end it all",
        "want to die", "wanna die", "better off dead", "no reason to live",
        "self harm", "self-harm", "hurt myself", "hurting myself",
        "don't want to be alive", "dont want to be alive", "not worth living",
    )

    fun screen(input: String): Level {
        val t = input.lowercase()
        return if (CRISIS_MARKERS.any { it in t }) Level.CRISIS else Level.NONE
    }

    /** Persona fully off. No score talk, no discipline framing. */
    fun supportMessage(): String = buildString {
        appendLine("This is bigger than any score, and the app stops mattering right now.")
        appendLine()
        appendLine("Please talk to a real person:")
        appendLine("• findahelpline.com — free, confidential helplines in your country")
        appendLine("• US & Canada: call or text 988")
        appendLine("• UK & ROI: Samaritans, 116 123")
        appendLine()
        append("If you are in immediate danger, call your local emergency number.")
    }
}
