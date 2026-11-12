package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Subject
import com.example.ui.theme.AccentIndigo
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark

@Composable
fun SubjectCard(
  subject: Subject,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val icon = getSubjectIcon(subject.code, subject.name, subject.isPractical)
  val badgeColor = if (subject.isPractical) AccentTeal else RoyalBlue800
  val badgeBg = if (subject.isPractical) AccentTeal.copy(alpha = 0.12f) else SkyBlueSoft

  OutlinedCard(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("subject_card_${subject.code}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    border = BorderStroke(1.dp, SlateBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(48.dp)
          .background(badgeBg, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = badgeColor,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            color = badgeBg,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = subject.code,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = badgeColor
              ),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Surface(
            color = if (subject.isPractical) AccentTeal.copy(alpha = 0.08f) else SkyBlueSoft.copy(alpha = 0.6f),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = if (subject.isPractical) "Practical Lab" else "Theory",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = if (subject.isPractical) AccentTeal else RoyalBlue800
              ),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = subject.name,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = TextPrimaryDark
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "${subject.units.size} Syllabus Units",
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextMuted
          )
        )
      }

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
        contentDescription = "View Subject Details",
        tint = TextMuted,
        modifier = Modifier.size(16.dp)
      )
    }
  }
}

fun getSubjectIcon(code: String, name: String, isPractical: Boolean): ImageVector {
  val lower = (code + " " + name).lowercase()
  return when {
    lower.contains("math") -> Icons.Default.Functions
    lower.contains("java") || lower.contains("python") -> Icons.Default.Code
    lower.contains("operating system") || lower.contains("os") -> Icons.Default.Terminal
    lower.contains("data structure") -> Icons.Default.DataObject
    lower.contains("artificial intelligence") || lower.contains("ai") || lower.contains("neural") -> Icons.Default.Psychology
    lower.contains("network") || lower.contains("iot") -> Icons.Default.Hub
    lower.contains("web") -> Icons.Default.Web
    lower.contains("dbms") || lower.contains("database") -> Icons.Default.Storage
    lower.contains("machine learning") || lower.contains("vision") || lower.contains("nlp") -> Icons.Default.Memory
    isPractical -> Icons.Default.Science
    else -> Icons.Default.Code
  }
}
