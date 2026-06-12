package com.forge.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Checkpoint overlay (SYSTEM_ALERT_WINDOW): shown when a restricted
 * category surfaces. States the cost from Checkpoint.prompt(), holds the
 * Proceed button inert for the 10-second wait, and logs the outcome as a
 * CheckpointEvent either way — deflections are visible progress.
 *
 * Build order: Week 5–9 (interventions). UI lands with the overlay
 * compose host; this service owns window attach/detach + outcome logging.
 */
class CheckpointOverlayService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO(interventions): attach ComposeView via WindowManager with
        // TYPE_APPLICATION_OVERLAY, render Checkpoint.prompt(pillar),
        // start the 10s timer, write LedgerEntry.CheckpointEvent on choice.
        return START_NOT_STICKY
    }
}
