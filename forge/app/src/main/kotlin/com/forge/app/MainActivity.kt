package com.forge.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.forge.app.ui.DashboardScreen
import com.forge.app.ui.theme.ForgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = ForgeApp.from(this)
        setContent {
            ForgeTheme {
                DashboardScreen(scores = app.scores)
            }
        }
    }
}
