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
    primary = Navy80,
    onPrimary = Navy20,
    primaryContainer = Navy40,
    onPrimaryContainer = Navy90,
    secondary = AmberGold80,
    onSecondary = Color.Black,
    secondaryContainer = AmberGold30,
    onSecondaryContainer = AmberGold80,
    tertiary = Teal80,
    background = SurfaceDark,
    surface = Color(0xFF1E293B),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = CardBorderDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Navy40,
    onPrimary = Color.White,
    primaryContainer = Navy90,
    onPrimaryContainer = Navy30,
    secondary = AmberGold40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = Teal40,
    background = SurfaceLight,
    surface = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = CardBorderLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted collegiate theme by default
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
