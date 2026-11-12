package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.model.DownloadRecord
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.AccentCrimson
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadsScreen(
  downloads: List<DownloadRecord>,
  onNavigateBack: () -> Unit,
  onOpenDownload: (DownloadRecord) -> Unit,
  onDeleteDownload: (Long) -> Unit,
  onBrowseSubjects: () -> Unit,
  modifier: Modifier = Modifier
) {
  var itemToDelete by remember { mutableStateOf<DownloadRecord?>(null) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Offline Downloads",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              )
            )
            Text(
              text = "${downloads.size} materials saved for offline reading",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("downloads_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    containerColor = EducationBackground
  ) { innerPadding ->
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("downloads_screen_content")
    ) {
      if (downloads.isEmpty()) {
        EmptyStateView(
          message = "No offline downloads yet",
          subMessage = "You can download any syllabus notes, question papers, or study materials to read offline without an active internet connection.",
          icon = Icons.Default.DownloadDone,
          actionLabel = "Browse Subjects",
          onActionClick = onBrowseSubjects
        )
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            OutlinedCard(
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.outlinedCardColors(
                containerColor = SkyBlueSoft.copy(alpha = 0.4f)
              ),
              border = BorderStroke(1.dp, SlateBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.DownloadDone,
                  contentDescription = null,
                  tint = RoyalBlue800,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Offline Storage Active",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = RoyalBlue900
                    )
                  )
                  Text(
                    text = "All downloaded files below are accessible anywhere, anytime without internet.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                  )
                }
              }
            }
          }

          items(downloads, key = { it.id }) { record ->
            DownloadItemCard(
              record = record,
              onOpenClick = { onOpenDownload(record) },
              onDeleteClick = { itemToDelete = record }
            )
          }
        }
      }
    }
  }

  itemToDelete?.let { record ->
    AlertDialog(
      onDismissRequest = { itemToDelete = null },
      title = { Text(text = "Remove Download") },
      text = { Text("Do you want to delete '${record.title}' from your offline downloads?") },
      confirmButton = {
        Button(
          onClick = {
            onDeleteDownload(record.id)
            itemToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = AccentCrimson)
        ) {
          Text("Delete", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(onClick = { itemToDelete = null }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun DownloadItemCard(
  record: DownloadRecord,
  onOpenClick: () -> Unit,
  onDeleteClick: () -> Unit
) {
  val dateStr = remember(record.downloadedAtMillis) {
    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(record.downloadedAtMillis))
  }

  OutlinedCard(
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, SlateBorder),
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("download_record_${record.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = SkyBlueSoft,
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = record.subjectCode,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue800
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        IconButton(
          onClick = onDeleteClick,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "Delete download",
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = record.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "${record.contentType.replace("_", " ")} • ${record.fileSizeFormatted} • Saved on $dateStr",
        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
      )

      Spacer(modifier = Modifier.height(14.dp))

      FilledTonalButton(
        onClick = onOpenClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
          containerColor = SkyBlueSoft,
          contentColor = RoyalBlue800
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("open_download_button_${record.id}")
      ) {
        Icon(
          imageVector = Icons.Default.Visibility,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "Open in In-App Reader", fontWeight = FontWeight.SemiBold)
      }
    }
  }
}
