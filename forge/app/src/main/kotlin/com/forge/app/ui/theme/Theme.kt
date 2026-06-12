package com.forge.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Dark, typographic, luxury-receipt. There is no light theme. */
object ForgeColors {
    val Background = Color(0xFF0A0A0B)
    val Surface = Color(0xFF121214)
    val Ink = Color(0xFFE8E4DC)
    val InkDim = Color(0xFF8A867E)
    val Rising = Color(0xFF4ADE80)
    val Falling = Color(0xFFEF4444)
    val Seal = Color(0xFFC8A24A)
}

private val Scheme = darkColorScheme(
    background = ForgeColors.Background,
    surface = ForgeColors.Surface,
    onBackground = ForgeColors.Ink,
    onSurface = ForgeColors.Ink,
    primary = ForgeColors.Ink,
    secondary = ForgeColors.InkDim,
)

@Composable
fun ForgeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, content = content)
}
