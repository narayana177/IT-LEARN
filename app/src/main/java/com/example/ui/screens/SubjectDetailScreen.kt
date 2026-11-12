package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.ContentType
import com.example.model.EducationalContent
import com.example.model.ExamType
import com.example.model.Subject
import com.example.model.UnitTopic
import com.example.ui.components.ContentCard
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
  subjectCode: String,
  initialTab: Int,
  allContent: List<EducationalContent>,
  downloadedContentIds: Set<Long>,
  downloadingProgress: Map<Long, Int>,
  isAdminLoggedIn: Boolean,
  onNavigateBack: () -> Unit,
  onViewContent: (EducationalContent) -> Unit,
  onDownloadContent: (EducationalContent) -> Unit,
  onEditContent: ((EducationalContent) -> Unit)?,
  onDeleteContent: ((Long) -> Unit)?,
  onAdminUploadClick: (String) -> Unit,
  onTopicClick: (Subject, UnitTopic) -> Unit,
  modifier: Modifier = Modifier
) {
  val subject = SyllabusData.getSubjectByCode(subjectCode)

  var selectedTab by remember { mutableIntStateOf(initialTab) }
  var selectedExamFilter by remember { mutableStateOf<String?>(null) }

  val tabs = listOf(
    "Syllabus & Units",
    "Notes",
    "Question Papers",
    "Important Questions",
    "Study Materials"
  )

  val subjectContent = remember(allContent, subjectCode) {
    allContent.filter { it.subjectCode.equals(subjectCode, ignoreCase = true) }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "${subject?.code}: ${subject?.name ?: "Subject Details"}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              ),
              maxLines = 1
            )
            Text(
              text = if (subject?.isPractical == true) "Practical Lab" else "Theory Subject",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("subject_detail_back_button")
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
    floatingActionButton = {
      // ONLY Admin sees upload button! Students NEVER see upload button
      if (isAdminLoggedIn) {
        ExtendedFloatingActionButton(
          onClick = { onAdminUploadClick(subjectCode) },
          containerColor = RoyalBlue800,
          contentColor = androidx.compose.ui.graphics.Color.White,
          icon = { Icon(Icons.Default.Add, contentDescription = "Upload") },
          text = { Text("Upload Content", fontWeight = FontWeight.Bold) },
          modifier = Modifier.testTag("admin_upload_fab")
        )
      }
    },
    containerColor = EducationBackground
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("subject_detail_screen")
    ) {
      PrimaryScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = RoyalBlue800,
        edgePadding = 16.dp,
        modifier = Modifier.testTag("subject_detail_tabs")
      ) {
        tabs.forEachIndexed { index, tabTitle ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = tabTitle,
                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium
              )
            }
          )
        }
      }

      when (selectedTab) {
        0 -> {
          // Tab 0: Syllabus & Units
          SyllabusUnitsTab(
            subject = subject,
            onUnitClick = { unit ->
              if (subject != null) onTopicClick(subject, unit)
            }
          )
        }
        1 -> {
          // Tab 1: Notes
          val notesList = remember(subjectContent) {
            subjectContent.filter { it.contentType == ContentType.NOTES.name }
          }

          if (notesList.isEmpty()) {
            EmptyStateView(
              message = "No content available yet",
              subMessage = "Official verified notes for ${subject?.code} have not been published by Admin yet. Check back soon.",
              actionLabel = if (isAdminLoggedIn) "Upload Notes Now" else null,
              onActionClick = if (isAdminLoggedIn) { { onAdminUploadClick(subjectCode) } } else null
            )
          } else {
            ContentList(
              items = notesList,
              isAdmin = isAdminLoggedIn,
              downloadedIds = downloadedContentIds,
              downloadingProgress = downloadingProgress,
              onViewClick = onViewContent,
              onDownloadClick = onDownloadContent,
              onEditClick = onEditContent,
              onDeleteClick = onDeleteContent
            )
          }
        }
        2 -> {
          // Tab 2: Question Papers (Mid-1, Mid-2, Regular, Supply, Previous)
          val allPapers = remember(subjectContent) {
            subjectContent.filter { it.contentType == ContentType.QUESTION_PAPER.name }
          }

          val filteredPapers = remember(allPapers, selectedExamFilter) {
            if (selectedExamFilter == null) allPapers
            else allPapers.filter { it.examType == selectedExamFilter }
          }

          Column(modifier = Modifier.fillMaxSize()) {
            // Exam category filter chips
            LazyRow(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              item {
                FilterChip(
                  selected = selectedExamFilter == null,
                  onClick = { selectedExamFilter = null },
                  label = { Text("All Papers (${allPapers.size})") }
                )
              }
              item {
                FilterChip(
                  selected = selectedExamFilter == ExamType.MID_1.name,
                  onClick = { selectedExamFilter = ExamType.MID_1.name },
                  label = { Text("Mid-1") }
                )
              }
              item {
                FilterChip(
                  selected = selectedExamFilter == ExamType.MID_2.name,
                  onClick = { selectedExamFilter = ExamType.MID_2.name },
                  label = { Text("Mid-2") }
                )
              }
              item {
                FilterChip(
                  selected = selectedExamFilter == ExamType.REGULAR.name,
                  onClick = { selectedExamFilter = ExamType.REGULAR.name },
                  label = { Text("Regular Exams") }
                )
              }
              item {
                FilterChip(
                  selected = selectedExamFilter == ExamType.SUPPLY.name,
                  onClick = { selectedExamFilter = ExamType.SUPPLY.name },
                  label = { Text("Supply Papers") }
                )
              }
              item {
                FilterChip(
                  selected = selectedExamFilter == ExamType.PREVIOUS.name,
                  onClick = { selectedExamFilter = ExamType.PREVIOUS.name },
                  label = { Text("Previous Papers") }
                )
              }
            }

            if (filteredPapers.isEmpty()) {
              EmptyStateView(
                message = "No paper available yet.",
                subMessage = "Official examination question papers will be published once verified by Admin. No mock/fake papers are allowed.",
                icon = Icons.Default.Quiz,
                actionLabel = if (isAdminLoggedIn) "Upload Exam Paper" else null,
                onActionClick = if (isAdminLoggedIn) { { onAdminUploadClick(subjectCode) } } else null
              )
            } else {
              ContentList(
                items = filteredPapers,
                isAdmin = isAdminLoggedIn,
                downloadedIds = downloadedContentIds,
                downloadingProgress = downloadingProgress,
                onViewClick = onViewContent,
                onDownloadClick = onDownloadContent,
                onEditClick = onEditContent,
                onDeleteClick = onDeleteContent
              )
            }
          }
        }
        3 -> {
          // Tab 3: Important Questions
          val questionsList = remember(subjectContent) {
            subjectContent.filter { it.contentType == ContentType.IMPORTANT_QUESTIONS.name }
          }

          if (questionsList.isEmpty()) {
            EmptyStateView(
              message = "No content available yet",
              subMessage = "Important exam questions and model solutions for ${subject?.code} will appear here once published by faculty.",
              actionLabel = if (isAdminLoggedIn) "Upload Questions" else null,
              onActionClick = if (isAdminLoggedIn) { { onAdminUploadClick(subjectCode) } } else null
            )
          } else {
            ContentList(
              items = questionsList,
              isAdmin = isAdminLoggedIn,
              downloadedIds = downloadedContentIds,
              downloadingProgress = downloadingProgress,
              onViewClick = onViewContent,
              onDownloadClick = onDownloadContent,
              onEditClick = onEditContent,
              onDeleteClick = onDeleteContent
            )
          }
        }
        4 -> {
          // Tab 4: Study Materials & Lab Manuals
          val materialsList = remember(subjectContent) {
            subjectContent.filter {
              it.contentType == ContentType.STUDY_MATERIAL.name ||
                it.contentType == ContentType.LAB_MANUAL.name ||
                it.contentType == ContentType.OTHER.name
            }
          }

          if (materialsList.isEmpty()) {
            EmptyStateView(
              message = "No content available yet",
              subMessage = "Textbook references, lab manuals, and supplementary study materials for ${subject?.code} will be provided here by Admin.",
              actionLabel = if (isAdminLoggedIn) "Upload Material" else null,
              onActionClick = if (isAdminLoggedIn) { { onAdminUploadClick(subjectCode) } } else null
            )
          } else {
            ContentList(
              items = materialsList,
              isAdmin = isAdminLoggedIn,
              downloadedIds = downloadedContentIds,
              downloadingProgress = downloadingProgress,
              onViewClick = onViewContent,
              onDownloadClick = onDownloadContent,
              onEditClick = onEditContent,
              onDeleteClick = onDeleteContent
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SyllabusUnitsTab(
  subject: Subject?,
  onUnitClick: (UnitTopic) -> Unit
) {
  if (subject == null || subject.units.isEmpty()) {
    EmptyStateView(
      message = "No syllabus units available",
      subMessage = "Curriculum outline is being compiled."
    )
    return
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      OutlinedCard(
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, SlateBorder),
        colors = CardDefaults.outlinedCardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Course Description",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900
            )
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = subject.description,
            style = MaterialTheme.typography.bodyMedium.copy(
              color = TextMuted,
              lineHeight = 20.sp
            )
          )
        }
      }
    }

    item {
      Text(
        text = "Curriculum Units & Topics",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = RoyalBlue900
        ),
        modifier = Modifier.padding(vertical = 4.dp)
      )
    }

    items(subject.units) { unit ->
      UnitItemCard(unit = unit, onClick = { onUnitClick(unit) })
    }
  }
}

@Composable
private fun UnitItemCard(
  unit: UnitTopic,
  onClick: () -> Unit
) {
  OutlinedCard(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag("unit_card_${unit.unitNumber}"),
    shape = RoundedCornerShape(14.dp),
    border = BorderStroke(1.dp, SlateBorder),
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
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
            text = "Unit ${unit.unitNumber}",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue800
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Text(
          text = "${unit.topics.size} Topics",
          style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = unit.unitTitle,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Preview top 3 topics
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        unit.topics.take(3).forEach { topic ->
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .background(RoyalBlue800, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = topic,
              style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
              maxLines = 1
            )
          }
        }
        if (unit.topics.size > 3) {
          Text(
            text = "+ ${unit.topics.size - 3} more topics in this unit",
            style = MaterialTheme.typography.labelSmall.copy(
              color = RoyalBlue800,
              fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(start = 14.dp, top = 2.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun ContentList(
  items: List<EducationalContent>,
  isAdmin: Boolean,
  downloadedIds: Set<Long>,
  downloadingProgress: Map<Long, Int>,
  onViewClick: (EducationalContent) -> Unit,
  onDownloadClick: (EducationalContent) -> Unit,
  onEditClick: ((EducationalContent) -> Unit)?,
  onDeleteClick: ((Long) -> Unit)?
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    items(items, key = { it.id }) { content ->
      ContentCard(
        content = content,
        isAdmin = isAdmin,
        isDownloaded = downloadedIds.contains(content.id),
        downloadProgress = downloadingProgress[content.id],
        onViewClick = { onViewClick(content) },
        onDownloadClick = { onDownloadClick(content) },
        onEditClick = if (onEditClick != null) { { onEditClick(content) } } else null,
        onDeleteClick = if (onDeleteClick != null) { { onDeleteClick(content.id) } } else null
      )
    }
  }
}
