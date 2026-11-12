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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.StudyDiscussion
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupStudyScreen(
  viewModel: MainViewModel,
  initialSubjectCode: String?,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedSubjectCode by remember {
    mutableStateOf(initialSubjectCode ?: SyllabusData.allSubjects.first().code)
  }

  val discussionsFlow = remember(selectedSubjectCode) {
    viewModel.getDiscussionsForSubject(selectedSubjectCode)
  }
  val discussions by discussionsFlow.collectAsState(initial = emptyList())

  var showPostDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Group Study Circle",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              )
            )
            Text(
              text = "Peer discussions & study collaboration",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("group_study_back_button")
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
      ExtendedFloatingActionButton(
        onClick = { showPostDialog = true },
        containerColor = RoyalBlue800,
        contentColor = androidx.compose.ui.graphics.Color.White,
        icon = { Icon(Icons.Default.AddComment, contentDescription = null) },
        text = { Text("Ask Question / Post", fontWeight = FontWeight.Bold) },
        modifier = Modifier.testTag("group_study_post_fab")
      )
    },
    containerColor = EducationBackground
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("group_study_screen_content")
    ) {
      // Compliance Notice: Peer Discussion vs Official Content
      Surface(
        color = SkyBlueSoft.copy(alpha = 0.5f),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = RoyalBlue800,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Peer Study Forum: Student discussion board. Official syllabus & notes cannot be modified.",
            style = MaterialTheme.typography.labelSmall.copy(
              color = RoyalBlue900,
              fontSize = 11.sp
            )
          )
        }
      }

      // Subject Filter Chips
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(SyllabusData.allSubjects, key = { it.code }) { subject ->
          FilterChip(
            selected = selectedSubjectCode == subject.code,
            onClick = { selectedSubjectCode = subject.code },
            label = { Text("${subject.code} ${subject.name.take(15)}") }
          )
        }
      }

      if (discussions.isEmpty()) {
        EmptyStateView(
          message = "No study discussions in $selectedSubjectCode yet",
          subMessage = "Be the first to ask a question, share an exam study tip, or clarify a doubt with your C-23 batchmates.",
          icon = Icons.Default.Forum,
          actionLabel = "Start a Discussion",
          onActionClick = { showPostDialog = true }
        )
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(discussions, key = { it.id }) { discussion ->
            DiscussionCard(discussion = discussion)
          }
        }
      }
    }
  }

  if (showPostDialog) {
    PostDiscussionDialog(
      subjectCode = selectedSubjectCode,
      onDismiss = { showPostDialog = false },
      onPost = { author, title, msg ->
        viewModel.postDiscussionMessage(selectedSubjectCode, author, title, msg)
        showPostDialog = false
      }
    )
  }
}

@Composable
private fun DiscussionCard(discussion: StudyDiscussion) {
  val dateStr = remember(discussion.timestamp) {
    SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(discussion.timestamp))
  }

  OutlinedCard(
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, SlateBorder),
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .background(SkyBlueSoft, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = RoyalBlue800,
              modifier = Modifier.size(18.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = discussion.authorName,
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = TextPrimaryDark
            )
          )
        }

        Text(
          text = dateStr,
          style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = discussion.title,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          color = RoyalBlue900
        )
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = discussion.message,
        style = MaterialTheme.typography.bodyMedium.copy(
          color = TextPrimaryDark,
          lineHeight = 20.sp
        )
      )
    }
  }
}

@Composable
private fun PostDiscussionDialog(
  subjectCode: String,
  onDismiss: () -> Unit,
  onPost: (String, String, String) -> Unit
) {
  var author by remember { mutableStateOf("") }
  var title by remember { mutableStateOf("") }
  var message by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = "Post in $subjectCode Discussion") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = author,
          onValueChange = { author = it },
          label = { Text("Your Name (Optional)") },
          placeholder = { Text("e.g. Satwik (C-23 Student)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Question Title / Topic *") },
          placeholder = { Text("e.g. Unit 2: Method Overriding vs Overloading") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = message,
          onValueChange = { message = it },
          label = { Text("Details / Doubt Explanation *") },
          placeholder = { Text("Describe what you'd like to understand...") },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() && message.isNotBlank()) {
            onPost(author, title, message)
          }
        },
        enabled = title.isNotBlank() && message.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue800)
      ) {
        Text("Post Question")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
