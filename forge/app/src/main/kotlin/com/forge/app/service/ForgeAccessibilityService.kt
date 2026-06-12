package com.forge.app.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Detection layer: window-state changes tell us when a restricted app
 * (or a browser, for local domain-list matching against maintained
 * open-source blocklists) comes to the foreground, so the Checkpoint or
 * Iron block can fire.
 *
 * Hard rule: canRetrieveWindowContent=false in the service config. We
 * react to which surface is foreground — we never read what's on it.
 */
class ForgeAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        // TODO(interventions): packageName → category via AppCategorizer;
        // if category restricted by active Checkpoint policy or Iron
        // commitment, start CheckpointOverlayService / block.
    }

    override fun onInterrupt() = Unit
}
