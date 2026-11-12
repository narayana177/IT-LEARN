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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ContentType
import com.example.model.EducationalContent
import com.example.ui.components.ContentCard
import com.example.ui.components.EmptyStateView
import com.example.ui.theme.AccentCrimson
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.IceBlue
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPortalScreen(
  viewModel: MainViewModel,
  onNavigateBack: () -> Unit,
  onNavigateToUpload: (String?) -> Unit,
  onEditContent: (EducationalContent) -> Unit,
  onViewContent: (EducationalContent) -> Unit,
  modifier: Modifier = Modifier
) {
  val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
  val allContent by viewModel.allContent.collectAsState()
  val downloadedRecords by viewModel.allDownloads.collectAsState()
  val downloadedIds = remember(downloadedRecords) { downloadedRecords.map { it.contentId }.toSet() }

  var filterQuery by remember { mutableStateOf("") }
  var showChangePasscodeDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = if (isAdminLoggedIn) "Admin Content Control" else "Admin Portal Access",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              )
            )
            Text(
              text = if (isAdminLoggedIn) "Authorized C-23 Administrator" else "Restricted Faculty Access",
              style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("admin_portal_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
          if (isAdminLoggedIn) {
            IconButton(
              onClick = { showChangePasscodeDialog = true },
              modifier = Modifier.testTag("admin_change_passcode_button")
            ) {
              Icon(Icons.Default.Key, contentDescription = "Change Passcode", tint = RoyalBlue800)
            }
            IconButton(
              onClick = { viewModel.logoutAdmin() },
              modifier = Modifier.testTag("admin_logout_button")
            ) {
              Icon(Icons.Default.Logout, contentDescription = "Logout Admin", tint = AccentCrimson)
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    floatingActionButton = {
      if (isAdminLoggedIn) {
        ExtendedFloatingActionButton(
          onClick = { onNavigateToUpload(null) },
          containerColor = RoyalBlue800,
          contentColor = Color.White,
          icon = { Icon(Icons.Default.Add, contentDescription = null) },
          text = { Text("Upload Content", fontWeight = FontWeight.Bold) },
          modifier = Modifier.testTag("admin_new_upload_fab")
        )
      }
    },
    containerColor = EducationBackground
  ) { innerPadding ->
    Box(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .testTag("admin_portal_content")
    ) {
      if (!isAdminLoggedIn) {
        // Admin Passcode Gate (Students never have login screens; only admins enter passcode here)
        AdminPasscodeGate(
          onAuthenticate = { passcode ->
            viewModel.authenticateAdmin(passcode)
          }
        )
      } else {
        // Authenticated Admin Dashboard
        val filteredList = remember(allContent, filterQuery) {
          if (filterQuery.isBlank()) allContent
          else allContent.filter {
            it.title.contains(filterQuery, ignoreCase = true) ||
              it.subjectCode.contains(filterQuery, ignoreCase = true) ||
              it.contentType.contains(filterQuery, ignoreCase = true) ||
              it.description.contains(filterQuery, ignoreCase = true)
          }
        }

        val notesCount = remember(allContent) { allContent.count { it.contentType == ContentType.NOTES.name } }
        val papersCount = remember(allContent) { allContent.count { it.contentType == ContentType.QUESTION_PAPER.name } }

        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Stat Overview Cards
          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              AdminStatCard(
                title = "Total Uploads",
                count = "${allContent.size}",
                modifier = Modifier.weight(1f)
              )
              AdminStatCard(
                title = "Study Notes",
                count = "$notesCount",
                modifier = Modifier.weight(1f)
              )
              AdminStatCard(
                title = "Exam Papers",
                count = "$papersCount",
                modifier = Modifier.weight(1f)
              )
            }
          }

          // Search & Filter Uploaded Files
          item {
            OutlinedTextField(
              value = filterQuery,
              onValueChange = { filterQuery = it },
              placeholder = { Text("Search your uploaded files...") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = RoyalBlue800) },
              singleLine = true,
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_search_uploads_input")
            )
          }

          item {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Published Educational Content (${filteredList.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = RoyalBlue900
                )
              )
            }
          }

          if (filteredList.isEmpty()) {
            item {
              EmptyStateView(
                message = "No content uploaded yet",
                subMessage = "Tap '+ Upload Content' button below to add subject-wise PDF files, question papers, notes, or study materials.",
                actionLabel = "+ Upload New Material",
                onActionClick = { onNavigateToUpload(null) }
              )
            }
          } else {
            items(filteredList, key = { it.id }) { content ->
              ContentCard(
                content = content,
                isAdmin = true,
                isDownloaded = downloadedIds.contains(content.id),
                downloadProgress = null,
                onViewClick = { onViewContent(content) },
                onDownloadClick = { viewModel.downloadFile(content) },
                onEditClick = { onEditContent(content) },
                onDeleteClick = { viewModel.deleteAdminContent(content.id) }
              )
            }
          }
        }
      }
    }
  }

  if (showChangePasscodeDialog) {
    ChangePasscodeDialog(
      onDismiss = { showChangePasscodeDialog = false },
      onChange = { oldPass, newPass ->
        val success = viewModel.updateAdminPasscode(oldPass, newPass)
        if (success) {
          showChangePasscodeDialog = false
        }
      }
    )
  }
}

@Composable
private fun AdminStatCard(
  title: String,
  count: String,
  modifier: Modifier = Modifier
) {
  OutlinedCard(
    shape = RoundedCornerShape(14.dp),
    border = BorderStroke(1.dp, SlateBorder),
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = count,
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.Bold,
          color = RoyalBlue800
        )
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextMuted,
          fontSize = 11.sp
        )
      )
    }
  }
}

@Composable
private fun AdminPasscodeGate(
  onAuthenticate: (String) -> Boolean
) {
  var passcode by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Surface(
      shape = CircleShape,
      color = SkyBlueSoft,
      modifier = Modifier.size(80.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = Icons.Default.Security,
          contentDescription = null,
          tint = RoyalBlue800,
          modifier = Modifier.size(40.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "Admin Panel Authentication",
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Bold,
        color = RoyalBlue900
      )
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Restricted to authorized faculty & administrators. Students have full direct access to all study materials on Home Screen without login.",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = TextMuted,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      ),
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedCard(
      shape = RoundedCornerShape(16.dp),
      border = BorderStroke(1.dp, SlateBorder),
      colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        OutlinedTextField(
          value = passcode,
          onValueChange = {
            passcode = it
            errorMessage = null
          },
          label = { Text("Admin Passcode") },
          placeholder = { Text("Enter secret passcode") },
          singleLine = true,
          visualTransformation = PasswordVisualTransformation(),
          isError = errorMessage != null,
          supportingText = {
            if (errorMessage != null) {
              Text(errorMessage!!, color = AccentCrimson)
            } else {
              Text("Default administrator passcode: c23admin")
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_passcode_input")
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            val success = onAuthenticate(passcode)
            if (!success) {
              errorMessage = "Incorrect passcode. Access denied."
            }
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = RoyalBlue800,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("admin_passcode_submit_button")
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Unlock Admin Management", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun ChangePasscodeDialog(
  onDismiss: () -> Unit,
  onChange: (String, String) -> Unit
) {
  var oldPasscode by remember { mutableStateOf("") }
  var newPasscode by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Change Admin Passcode") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = oldPasscode,
          onValueChange = { oldPasscode = it },
          label = { Text("Current Passcode") },
          visualTransformation = PasswordVisualTransformation(),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = newPasscode,
          onValueChange = { newPasscode = it },
          label = { Text("New Passcode (min 6 chars)") },
          visualTransformation = PasswordVisualTransformation(),
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onChange(oldPasscode, newPasscode) },
        enabled = oldPasscode.isNotBlank() && newPasscode.length >= 6
      ) {
        Text("Save Passcode")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
