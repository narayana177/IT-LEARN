package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark

@Composable
fun EmptyStateView(
  message: String,
  subMessage: String = "Only official materials uploaded and verified by Admin are displayed here.",
  icon: ImageVector = Icons.Default.FolderOff,
  actionLabel: String? = null,
  onActionClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  ElevatedCard(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("empty_state_view"),
    shape = RoundedCornerShape(20.dp),
    colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = androidx.compose.material3.CardDefaults.elevatedCardElevation(
      defaultElevation = 2.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(72.dp)
          .background(SkyBlueSoft, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = RoyalBlue800,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = message,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark,
          letterSpacing = 0.2.sp
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = subMessage,
        style = MaterialTheme.typography.bodyMedium.copy(
          color = TextMuted,
          lineHeight = 20.sp
        ),
        textAlign = TextAlign.Center
      )

      if (actionLabel != null && onActionClick != null) {
        Spacer(modifier = Modifier.height(20.dp))
        FilledTonalButton(
          onClick = onActionClick,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = SkyBlueSoft,
            contentColor = RoyalBlue800
          ),
          modifier = Modifier.testTag("empty_state_action_button")
        ) {
          Text(text = actionLabel, fontWeight = FontWeight.SemiBold)
        }
      }
    }
  }
}
