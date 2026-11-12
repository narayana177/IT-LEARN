package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.ContentType
import com.example.model.EducationalContent
import com.example.model.Subject
import com.example.ui.components.ContentCard
import com.example.ui.components.EmptyStateView
import com.example.ui.components.SubjectCard
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
  viewModel: MainViewModel,
  onNavigateBack: () -> Unit,
  onNavigateToSubject: (String) -> Unit,
  onViewContent: (EducationalContent) -> Unit,
  onDownloadContent: (EducationalContent) -> Unit,
  modifier: Modifier = Modifier
) {
  val searchQuery by viewModel.searchQuery.collectAsState()
  val semesterFilter by viewModel.searchSemesterFilter.collectAsState()
  val categoryFilter by viewModel.searchCategoryFilter.collectAsState()
  val filteredContent by viewModel.filteredSearchResults.collectAsState()
  val downloadedRecords by viewModel.allDownloads.collectAsState()
  val downloadedIds = remember(downloadedRecords) { downloadedRecords.map { it.contentId }.toSet() }
  val downloadingProgress by viewModel.downloadingProgress.collectAsState()

  // Match syllabus subjects as well
  val matchedSubjects = remember(searchQuery, semesterFilter) {
    if (searchQuery.isBlank() && semesterFilter == null) {
      emptyList<Subject>()
    } else {
      SyllabusData.allSubjects.filter { subject ->
        val matchSem = semesterFilter == null || subject.semester == semesterFilter
        val matchQuery = searchQuery.isBlank() ||
          subject.code.contains(searchQuery, ignoreCase = true) ||
          subject.name.contains(searchQuery, ignoreCase = true) ||
          subject.units.any { u -> u.unitTitle.contains(searchQuery, ignoreCase = true) || u.topics.any { t -> t.contains(searchQuery, ignoreCase = true) } }
        matchSem && matchQuery
      }
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Search Syllabus & Papers",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900
            )
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("search_back_button")
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
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("search_screen_content")
    ) {
      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.searchQuery.value = it },
        placeholder = { Text("Search subjects, codes (AI-301), units, notes...") },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = RoyalBlue800)
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { viewModel.searchQuery.value = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = TextMuted)
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
          focusedContainerColor = MaterialTheme.colorScheme.surface,
          unfocusedContainerColor = MaterialTheme.colorScheme.surface,
          focusedIndicatorColor = RoyalBlue800,
          unfocusedIndicatorColor = SlateBorder
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
          .testTag("search_input_field")
      )

      // Semester Filter Chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          FilterChip(
            selected = semesterFilter == null,
            onClick = { viewModel.searchSemesterFilter.value = null },
            label = { Text("All Semesters") }
          )
        }
        items(listOf(3, 4, 5, 6)) { sem ->
          FilterChip(
            selected = semesterFilter == sem,
            onClick = {
              viewModel.searchSemesterFilter.value = if (semesterFilter == sem) null else sem
            },
            label = { Text("${sem}th Sem") }
          )
        }
      }

      // Content Type Filter Chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          FilterChip(
            selected = categoryFilter == null,
            onClick = { viewModel.searchCategoryFilter.value = null },
            label = { Text("All Types") }
          )
        }
        item {
          FilterChip(
            selected = categoryFilter == ContentType.NOTES.name,
            onClick = {
              viewModel.searchCategoryFilter.value =
                if (categoryFilter == ContentType.NOTES.name) null else ContentType.NOTES.name
            },
            label = { Text("Notes") }
          )
        }
        item {
          FilterChip(
            selected = categoryFilter == ContentType.QUESTION_PAPER.name,
            onClick = {
              viewModel.searchCategoryFilter.value =
                if (categoryFilter == ContentType.QUESTION_PAPER.name) null else ContentType.QUESTION_PAPER.name
            },
            label = { Text("Question Papers") }
          )
        }
        item {
          FilterChip(
            selected = categoryFilter == ContentType.IMPORTANT_QUESTIONS.name,
            onClick = {
              viewModel.searchCategoryFilter.value =
                if (categoryFilter == ContentType.IMPORTANT_QUESTIONS.name) null else ContentType.IMPORTANT_QUESTIONS.name
            },
            label = { Text("Important Questions") }
          )
        }
      }

      // Results List
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        if (matchedSubjects.isNotEmpty()) {
          item {
            Text(
              text = "Matching C-23 Subjects (${matchedSubjects.size})",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              )
            )
          }

          items(matchedSubjects, key = { "subj_${it.code}" }) { subject ->
            SubjectCard(
              subject = subject,
              onClick = { onNavigateToSubject(subject.code) }
            )
          }
        }

        if (filteredContent.isNotEmpty()) {
          item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Uploaded Materials & Papers (${filteredContent.size})",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              )
            )
          }

          items(filteredContent, key = { "content_${it.id}" }) { content ->
            ContentCard(
              content = content,
              isAdmin = false,
              isDownloaded = downloadedIds.contains(content.id),
              downloadProgress = downloadingProgress[content.id],
              onViewClick = { onViewContent(content) },
              onDownloadClick = { onDownloadContent(content) }
            )
          }
        } else if (matchedSubjects.isEmpty() && searchQuery.isNotBlank()) {
          item {
            EmptyStateView(
              message = "No matching syllabus content",
              subMessage = "No subjects or uploaded materials match '$searchQuery'. Try searching for 'Java', 'AI-301', 'Operating Systems', or 'DBMS'."
            )
          }
        } else if (searchQuery.isBlank() && matchedSubjects.isEmpty() && filteredContent.isEmpty()) {
          item {
            EmptyStateView(
              message = "Search IT Learn Library",
              subMessage = "Type a subject name, subject code, unit topic, or filter by semester to find study materials and previous papers."
            )
          }
        }
      }
    }
  }
}
