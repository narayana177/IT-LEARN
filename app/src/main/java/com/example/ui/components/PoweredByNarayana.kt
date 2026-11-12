package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.TextMuted

/**
 * Reusable "Powered by Narayana" branding component.
 * Adheres to:
 * - Displayed on splash screen and subtly at the bottom of the Home Screen.
 * - Clean, professional typography.
 * - Same blue-and-white branding style.
 * - Exact text: Powered by Narayana.
 */
@Composable
fun PoweredByNarayana(
  modifier: Modifier = Modifier,
  isSplash: Boolean = false
) {
  if (isSplash) {
    Surface(
      modifier = modifier.testTag("powered_by_narayana_splash"),
      color = SkyBlueSoft.copy(alpha = 0.35f),
      shape = RoundedCornerShape(24.dp)
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.School,
          contentDescription = null,
          tint = RoyalBlue800,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Powered by Narayana",
          style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
            color = RoyalBlue800
          )
        )
      }
    }
  } else {
    // Subtle footer on Home Screen
    Column(
      modifier = modifier
        .padding(vertical = 16.dp)
        .testTag("powered_by_narayana_footer"),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoStories,
          contentDescription = null,
          tint = TextMuted,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Powered by Narayana",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextMuted,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.3.sp
          ),
          textAlign = TextAlign.Center
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Diploma C-23 Academic Resource Portal",
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextMuted.copy(alpha = 0.8f),
          fontSize = 11.sp
        )
      )
    }
  }
}
