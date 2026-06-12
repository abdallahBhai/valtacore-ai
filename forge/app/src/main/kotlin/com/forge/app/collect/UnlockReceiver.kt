package com.forge.app.collect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.forge.app.ForgeApp
import java.time.Instant

/**
 * Unlock timestamps are the only input the sleep-window derivation needs:
 * last unlock at night, first unlock in the morning. No wearable required.
 */
class UnlockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_USER_PRESENT) {
            ForgeApp.from(context).recordUnlock(Instant.now())
        }
    }
}
