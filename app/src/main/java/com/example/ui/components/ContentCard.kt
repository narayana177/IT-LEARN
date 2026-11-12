package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ContentType
import com.example.model.EducationalContent
import com.example.model.ExamType
import com.example.ui.theme.AccentCrimson
import com.example.ui.theme.AccentIndigo
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ContentCard(
  content: EducationalContent,
  isAdmin: Boolean,
  isDownloaded: Boolean,
  downloadProgress: Int?,
  onViewClick: () -> Unit,
  onDownloadClick: () -> Unit,
  onEditClick: (() -> Unit)? = null,
  onDeleteClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  var showDeleteDialog by remember { mutableStateOf(false) }

  val typeColor = when (content.contentType) {
    ContentType.QUESTION_PAPER.name -> AccentCrimson
    ContentType.NOTES.name -> RoyalBlue800
    ContentType.IMPORTANT_QUESTIONS.name -> AccentIndigo
    ContentType.STUDY_MATERIAL.name -> AccentTeal
    else -> RoyalBlue800
  }

  val typeBadgeBg = typeColor.copy(alpha = 0.10f)

  OutlinedCard(
    modifier = modifier
      .fillMaxWidth()
      .testTag("content_card_${content.id}"),
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, SlateBorder),
    colors = androidx.compose.material3.CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header: Badges & Admin Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            color = typeBadgeBg,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = if (content.contentType == ContentType.QUESTION_PAPER.name && content.examType != ExamType.NONE.name) {
                content.examType.replace("_", " ")
              } else {
                content.contentType.replace("_", " ")
              },
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = typeColor
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }

          if (!content.year.isNullOrBlank()) {
            Surface(
              color = SkyBlueSoft,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "Year ${content.year}",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = RoyalBlue800
                ),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }

          if (content.unitNumber != null && content.unitNumber > 0) {
            Surface(
              color = SlateBorder.copy(alpha = 0.6f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "Unit ${content.unitNumber}",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Medium,
                  color = TextMuted
                ),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }
        }

        // Admin-only controls: Edit & Delete buttons (Students NEVER see these)
        if (isAdmin) {
          Row {
            if (onEditClick != null) {
              IconButton(
                onClick = onEditClick,
                modifier = Modifier
                  .size(36.dp)
                  .testTag("admin_edit_content_${content.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Edit Content",
                  tint = RoyalBlue800,
                  modifier = Modifier.size(18.dp)
                )
              }
            }

            if (onDeleteClick != null) {
              IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                  .size(36.dp)
                  .testTag("admin_delete_content_${content.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete Content",
                  tint = AccentCrimson,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title & description
      Text(
        text = content.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      if (content.description.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = content.description,
          style = MaterialTheme.typography.bodySmall.copy(
            color = TextMuted,
            lineHeight = 18.sp
          ),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Metadata Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        val dateStr = remember(content.uploadedAtMillis) {
          SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(content.uploadedAtMillis))
        }

        Text(
          text = "${content.subjectCode} • $dateStr",
          style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
        )

        Text(
          text = content.fileSizeFormatted,
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Medium,
            color = TextMuted
          )
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Action Buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        FilledTonalButton(
          onClick = onViewClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = SkyBlueSoft,
            contentColor = RoyalBlue800
          ),
          modifier = Modifier
            .weight(1f)
            .testTag("view_content_button_${content.id}")
        ) {
          Icon(
            imageVector = Icons.Default.Visibility,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "Read / View", fontWeight = FontWeight.SemiBold)
        }

        if (isDownloaded) {
          OutlinedButton(
            onClick = onViewClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = AccentTeal
            ),
            border = BorderStroke(1.dp, AccentTeal),
            modifier = Modifier
              .weight(1f)
              .testTag("downloaded_indicator_${content.id}")
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = AccentTeal,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Downloaded", fontWeight = FontWeight.SemiBold)
          }
        } else if (downloadProgress != null) {
          Button(
            onClick = { /* In progress */ },
            enabled = false,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(14.dp),
              strokeWidth = 2.dp,
              color = RoyalBlue800
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "$downloadProgress%", fontSize = 12.sp)
          }
        } else {
          Button(
            onClick = onDownloadClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = RoyalBlue800,
              contentColor = Color.White
            ),
            modifier = Modifier
              .weight(1f)
              .testTag("download_offline_button_${content.id}")
          ) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Download", fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }
  }

  if (showDeleteDialog && onDeleteClick != null) {
    AlertDialog(
      onDismissRequest = { showDeleteDialog = false },
      title = { Text(text = "Confirm Delete") },
      text = { Text("Are you sure you want to permanently delete '${content.title}'? This action cannot be undone.") },
      confirmButton = {
        Button(
          onClick = {
            showDeleteDialog = false
            onDeleteClick()
          },
          colors = ButtonDefaults.buttonColors(containerColor = AccentCrimson)
        ) {
          Text("Delete", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}
