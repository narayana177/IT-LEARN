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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.EducationalContent
import com.example.ui.components.ContentCard
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitTopicDetailScreen(
  subjectCode: String,
  unitNumber: Int,
  allContent: List<EducationalContent>,
  downloadedContentIds: Set<Long>,
  downloadingProgress: Map<Long, Int>,
  isAdminLoggedIn: Boolean,
  onNavigateBack: () -> Unit,
  onViewContent: (EducationalContent) -> Unit,
  onDownloadContent: (EducationalContent) -> Unit,
  modifier: Modifier = Modifier
) {
  val subject = SyllabusData.getSubjectByCode(subjectCode)
  val unit = subject?.units?.find { it.unitNumber == unitNumber }

  // Student study progress checkmarks for topics
  val topicCompleted = remember { mutableStateMapOf<Int, Boolean>() }

  val unitNotes = remember(allContent, subjectCode, unitNumber) {
    allContent.filter {
      it.subjectCode.equals(subjectCode, ignoreCase = true) &&
        it.unitNumber == unitNumber
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Unit $unitNumber: ${unit?.unitTitle ?: "Unit Overview"}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              ),
              maxLines = 1
            )
            Text(
              text = "${subject?.code} - ${subject?.name}",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("unit_detail_back_button")
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
    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("unit_topic_detail_content"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        OutlinedCard(
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, SlateBorder),
          colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Surface(
              color = SkyBlueSoft,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "C-23 Official Syllabus",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = RoyalBlue800
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = unit?.unitTitle ?: "Unit Topics",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "Study and track all official topics under Unit $unitNumber. Tap circle icon to mark topic as completed.",
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )
          }
        }
      }

      item {
        Text(
          text = "Prescribed Curriculum Topics",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = RoyalBlue900
          )
        )
      }

      itemsIndexed(unit?.topics ?: emptyList()) { index, topic ->
        val isDone = topicCompleted[index] == true
        OutlinedCard(
          shape = RoundedCornerShape(12.dp),
          border = BorderStroke(1.dp, if (isDone) AccentTeal else SlateBorder),
          colors = CardDefaults.outlinedCardColors(
            containerColor = if (isDone) SkyBlueSoft.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("topic_item_$index")
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = { topicCompleted[index] = !isDone },
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Toggle completion",
                tint = if (isDone) AccentTeal else TextMuted
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Topic ${index + 1}",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
              )
              Text(
                text = topic,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = if (isDone) FontWeight.Normal else FontWeight.SemiBold,
                  color = if (isDone) TextMuted else TextPrimaryDark
                )
              )
            }
          }
        }
      }

      if (unitNotes.isNotEmpty()) {
        item {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Unit $unitNumber Uploaded Materials (${unitNotes.size})",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900
            )
          )
        }

        itemsIndexed(unitNotes) { _, content ->
          ContentCard(
            content = content,
            isAdmin = isAdminLoggedIn,
            isDownloaded = downloadedContentIds.contains(content.id),
            downloadProgress = downloadingProgress[content.id],
            onViewClick = { onViewContent(content) },
            onDownloadClick = { onDownloadContent(content) }
          )
        }
      }
    }
  }
}
