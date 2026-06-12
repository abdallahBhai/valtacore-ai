package com.forge.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.forge.app.ScoreRepository
import com.forge.app.ui.theme.ForgeColors
import com.forge.core.model.Pillar
import com.forge.core.scoring.MomentumScore
import com.forge.core.scoring.Trend
import kotlin.math.roundToInt

/**
 * The three verified scores. A number, an arrow, nothing to argue with.
 */
@Composable
fun DashboardScreen(scores: ScoreRepository) {
    var momentum by remember { mutableStateOf<Map<Pillar, MomentumScore?>>(emptyMap()) }

    LaunchedEffect(Unit) {
        momentum = Pillar.entries.associateWith { scores.momentum(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Background)
            .padding(horizontal = 28.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "FORGE",
            color = ForgeColors.Ink,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 6.sp,
        )
        Text(
            text = "Your phone testifies.",
            color = ForgeColors.InkDim,
            fontSize = 12.sp,
        )
        Spacer(Modifier.height(24.dp))

        Pillar.entries.forEach { pillar ->
            ScoreRow(pillar, momentum[pillar])
        }
    }
}

@Composable
private fun ScoreRow(pillar: Pillar, score: MomentumScore?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ForgeColors.Surface)
            .padding(20.dp),
    ) {
        Text(
            text = pillar.name,
            color = ForgeColors.InkDim,
            fontSize = 11.sp,
            letterSpacing = 3.sp,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = score?.value?.roundToInt()?.toString() ?: "—",
                color = ForgeColors.Ink,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displayLarge,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = when (score?.trend) {
                    Trend.RISING -> "↗"
                    Trend.FALLING -> "↘"
                    Trend.HOLDING -> "→"
                    null -> ""
                },
                color = when (score?.trend) {
                    Trend.RISING -> ForgeColors.Rising
                    Trend.FALLING -> ForgeColors.Falling
                    else -> ForgeColors.InkDim
                },
                fontSize = 32.sp,
            )
        }
        if (score == null) {
            Text(
                text = "No verified data yet. The engine is watching.",
                color = ForgeColors.InkDim,
                fontSize = 11.sp,
            )
        }
    }
}
