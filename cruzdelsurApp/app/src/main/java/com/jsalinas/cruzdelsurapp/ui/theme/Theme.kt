package com.jsalinas.cruzdelsurapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulMarino,
    secondary = AzulMarino,
    secondaryContainer = GrisTarjeta,
    onSecondaryContainer = AzulMarino,
    tertiary = RojoAlerta,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White
)

@Composable
fun CruzDelSurTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
