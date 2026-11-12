package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = RoyalBlue800,
  onPrimary = CleanWhite,
  primaryContainer = SkyBlueSoft,
  onPrimaryContainer = RoyalBlue900,
  secondary = AzureBlue,
  onSecondary = CleanWhite,
  secondaryContainer = IceBlue,
  onSecondaryContainer = RoyalBlue800,
  tertiary = AccentTeal,
  onTertiary = CleanWhite,
  background = EducationBackground,
  onBackground = TextPrimaryDark,
  surface = CardSurfaceWhite,
  onSurface = TextPrimaryDark,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = TextSecondaryDark,
  outline = SlateBorder,
  outlineVariant = SlateBorder.copy(alpha = 0.5f)
)

private val DarkColorScheme = darkColorScheme(
  primary = RoyalBlue500,
  onPrimary = CleanWhite,
  primaryContainer = DarkSurfaceVariant,
  onPrimaryContainer = SkyBlueSoft,
  secondary = AzureBlue,
  onSecondary = CleanWhite,
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // For IT Learn we preserve the custom professional blue & white branding by default
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
