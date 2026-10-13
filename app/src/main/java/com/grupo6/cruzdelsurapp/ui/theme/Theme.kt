package com.grupo6.cruzdelsurapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CdsNavy,
    onPrimary = Color.White,
    primaryContainer = CdsNavyContainer,
    onPrimaryContainer = CdsNavy,
    secondary = CdsNavyLight,
    onSecondary = Color.White,
    secondaryContainer = CdsNavyContainer,
    onSecondaryContainer = CdsNavy,
    tertiary = CdsRed,
    onTertiary = Color.White,
    background = CdsBackground,
    onBackground = CdsText,
    surface = CdsSurface,
    onSurface = CdsText,
    surfaceVariant = CdsField,
    onSurfaceVariant = CdsTextMuted,
    surfaceContainer = CdsSurface,
    outline = CdsBorder,
    error = CdsAlert,
    errorContainer = CdsAlertContainer
)

/** Tema de Cruz del Sur: paleta clara de marca (sin color dinámico), igual que el prototipo. */
@Composable
fun CruzDelSurAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
