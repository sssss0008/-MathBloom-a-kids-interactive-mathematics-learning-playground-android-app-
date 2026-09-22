package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = MathBlueLight,
    onPrimary = MathBlueDark,
    primaryContainer = DarkCard,
    secondary = MathOrange,
    tertiary = MathGreen,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = Color.White,
    onSurface = Color.White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = MathBlue,
    onPrimary = Color.White,
    primaryContainer = MathBlueLight,
    onPrimaryContainer = MathBlueDark,
    secondary = MathOrange,
    onSecondary = Color.White,
    secondaryContainer = MathOrangeLight,
    tertiary = MathGreen,
    onTertiary = Color.White,
    tertiaryContainer = MathGreenLight,
    background = CanvasBackgroundLight,
    onBackground = TextPrimary,
    surface = CardBackgroundLight,
    onSurface = TextPrimary,
    surfaceVariant = MathBlueLight,
    onSurfaceVariant = TextSecondary
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep branded kid colors consistent
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

