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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.ui.components.EmptyStateView
import com.example.ui.components.SubjectCard
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterScreen(
  semesterNumber: Int,
  onNavigateBack: () -> Unit,
  onNavigateToSubject: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val semInfo = SyllabusData.semesters.find { it.semesterNumber == semesterNumber }
  val subjects = SyllabusData.getSubjectsForSemester(semesterNumber)
  val theorySubjects = remember(subjects) { subjects.filter { !it.isPractical } }
  val practicalSubjects = remember(subjects) { subjects.filter { it.isPractical } }

  var selectedTab by remember { mutableIntStateOf(0) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = semInfo?.title ?: "${semesterNumber}th Semester",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              )
            )
            Text(
              text = "Diploma C-23 Curriculum",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("semester_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back to Home"
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
        .testTag("semester_screen_content")
    ) {
      if (semInfo == null || !semInfo.isContentReady) {
        val semTitle = semInfo?.title ?: "${semesterNumber}th Semester"
        // 1st and 2nd Semesters: Exact compliance: "Syllabus content will be added soon. Do not invent subjects or syllabus content."
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(80.dp)
              .background(SkyBlueSoft, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              tint = RoyalBlue800,
              modifier = Modifier.size(42.dp)
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          Text(
            text = "Syllabus content will be added soon.",
            style = MaterialTheme.typography.headlineSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900,
              textAlign = TextAlign.Center
            ),
            modifier = Modifier.testTag("semester_notice_text")
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Official C-23 curriculum details for $semTitle are currently being updated by the state technical board. No unofficial subjects will be displayed.",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = TextMuted,
              lineHeight = 22.sp,
              textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
          )
        }
      } else {
        // Active Semesters (3rd, 4th, 5th, 6th)
        if (practicalSubjects.isNotEmpty() && theorySubjects.isNotEmpty()) {
          PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = RoyalBlue800,
            modifier = Modifier.testTag("semester_tabs")
          ) {
            Tab(
              selected = selectedTab == 0,
              onClick = { selectedTab = 0 },
              text = {
                Text(
                  text = "Theory (${theorySubjects.size})",
                  fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                )
              }
            )
            Tab(
              selected = selectedTab == 1,
              onClick = { selectedTab = 1 },
              text = {
                Text(
                  text = "Practicals (${practicalSubjects.size})",
                  fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                )
              }
            )
          }
        }

        val displayedSubjects = when {
          practicalSubjects.isEmpty() -> theorySubjects
          theorySubjects.isEmpty() -> practicalSubjects
          selectedTab == 0 -> theorySubjects
          else -> practicalSubjects
        }

        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(displayedSubjects, key = { it.code }) { subject ->
            SubjectCard(
              subject = subject,
              onClick = { onNavigateToSubject(subject.code) }
            )
          }
        }
      }
    }
  }
}
