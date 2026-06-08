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
    primary = ProfBlue,
    secondary = SlateSecondary,
    tertiary = EmeraldMute,
    background = SoftGraphite,
    surface = SoftGraphite,
    onPrimary = OnProfBlue,
    onSecondary = OnSlateSecondary,
    onBackground = SoftCoolGrey,
    onSurface = SoftCoolGrey,
    surfaceVariant = BorderLight.copy(alpha = 0.2f),
    onSurfaceVariant = OutlineGrey
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ProfBlue,
    secondary = SlateSecondary,
    tertiary = EmeraldMute,
    background = SoftCoolGrey,
    surface = Color.White,
    onPrimary = OnProfBlue,
    onSecondary = OnSlateSecondary,
    onBackground = SoftGraphite,
    onSurface = SoftGraphite,
    surfaceVariant = BorderLight,
    onSurfaceVariant = SoftGraphite,
    outline = OutlineGrey
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
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
