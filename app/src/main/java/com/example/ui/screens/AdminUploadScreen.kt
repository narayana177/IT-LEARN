package com.example.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.ContentType
import com.example.model.EducationalContent
import com.example.model.ExamType
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
fun AdminUploadScreen(
  editContentId: Long?,
  prefillSubjectCode: String?,
  allContent: List<EducationalContent>,
  onNavigateBack: () -> Unit,
  onSaveContent: (
    existingId: Long?,
    semester: Int,
    subjectCode: String,
    subjectName: String,
    unitNumber: Int?,
    topicName: String?,
    contentType: ContentType,
    examType: ExamType,
    year: String?,
    title: String,
    description: String,
    textContent: String,
    fileUri: Uri?,
    fileName: String?
  ) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val existingContent = remember(editContentId, allContent) {
    if (editContentId != null) allContent.find { it.id == editContentId } else null
  }

  var selectedSemester by remember {
    mutableIntStateOf(existingContent?.semester ?: 3)
  }

  val subjectsForSem = remember(selectedSemester) {
    SyllabusData.getSubjectsForSemester(selectedSemester)
  }

  var selectedSubjectCode by remember {
    mutableStateOf(
      existingContent?.subjectCode
        ?: prefillSubjectCode
        ?: subjectsForSem.firstOrNull()?.code
        ?: "AI-301"
    )
  }

  var selectedUnitNumber by remember {
    mutableStateOf(existingContent?.unitNumber?.toString() ?: "1")
  }

  var topicName by remember {
    mutableStateOf(existingContent?.topicName ?: "")
  }

  var selectedContentType by remember {
    mutableStateOf(
      existingContent?.let {
        ContentType.values().find { c -> c.name == it.contentType }
      } ?: ContentType.NOTES
    )
  }

  var selectedExamType by remember {
    mutableStateOf(
      existingContent?.let {
        ExamType.values().find { e -> e.name == it.examType }
      } ?: ExamType.MID_1
    )
  }

  var year by remember {
    mutableStateOf(existingContent?.year ?: "2025")
  }

  var title by remember {
    mutableStateOf(existingContent?.title ?: "")
  }

  var description by remember {
    mutableStateOf(existingContent?.description ?: "")
  }

  var textContent by remember {
    mutableStateOf(existingContent?.textContent ?: "")
  }

  var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
  var selectedFileName by remember { mutableStateOf<String?>(null) }

  // File Picker Launcher for PDF/docs
  val filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null) {
      selectedFileUri = uri
      var displayName = "uploaded_file.pdf"
      try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
          if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0) {
              displayName = cursor.getString(nameIndex)
            }
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
      selectedFileName = displayName
      if (title.isBlank()) {
        title = displayName.substringBeforeLast(".")
      }
    }
  }

  // Dropdown states
  var semesterExpanded by remember { mutableStateOf(false) }
  var subjectExpanded by remember { mutableStateOf(false) }
  var typeExpanded by remember { mutableStateOf(false) }
  var examExpanded by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (existingContent != null) "Edit Educational Material" else "Upload Official Content",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900
            )
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.testTag("upload_screen_back_button")
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
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Form Card
      OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SlateBorder),
        colors = CardDefaults.outlinedCardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = "Curriculum Organization",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900
            )
          )

          // 1. Semester Selector
          ExposedDropdownMenuBox(
            expanded = semesterExpanded,
            onExpandedChange = { semesterExpanded = it },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = "${selectedSemester}th Semester",
              onValueChange = {},
              readOnly = true,
              label = { Text("Semester (1st to 6th) *") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = semesterExpanded) },
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .testTag("upload_semester_dropdown")
            )
            ExposedDropdownMenu(
              expanded = semesterExpanded,
              onDismissRequest = { semesterExpanded = false }
            ) {
              (1..6).forEach { sem ->
                DropdownMenuItem(
                  text = { Text("${sem}th Semester") },
                  onClick = {
                    selectedSemester = sem
                    selectedSubjectCode = SyllabusData.getSubjectsForSemester(sem).firstOrNull()?.code ?: "AI-301"
                    semesterExpanded = false
                  }
                )
              }
            }
          }

          // 2. Subject Selector
          ExposedDropdownMenuBox(
            expanded = subjectExpanded,
            onExpandedChange = { subjectExpanded = it },
            modifier = Modifier.fillMaxWidth()
          ) {
            val currentSubjectName = SyllabusData.getSubjectByCode(selectedSubjectCode)?.name ?: ""
            OutlinedTextField(
              value = "$selectedSubjectCode - $currentSubjectName",
              onValueChange = {},
              readOnly = true,
              label = { Text("Subject (Official C-23) *") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectExpanded) },
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .testTag("upload_subject_dropdown")
            )
            ExposedDropdownMenu(
              expanded = subjectExpanded,
              onDismissRequest = { subjectExpanded = false }
            ) {
              subjectsForSem.forEach { subj ->
                DropdownMenuItem(
                  text = { Text("${subj.code} - ${subj.name}") },
                  onClick = {
                    selectedSubjectCode = subj.code
                    subjectExpanded = false
                  }
                )
              }
            }
          }

          // 3. Content Type Selector
          ExposedDropdownMenuBox(
            expanded = typeExpanded,
            onExpandedChange = { typeExpanded = it },
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedTextField(
              value = selectedContentType.displayName,
              onValueChange = {},
              readOnly = true,
              label = { Text("Content Type *") },
              trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
              modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .testTag("upload_content_type_dropdown")
            )
            ExposedDropdownMenu(
              expanded = typeExpanded,
              onDismissRequest = { typeExpanded = false }
            ) {
              ContentType.values().forEach { cType ->
                DropdownMenuItem(
                  text = { Text(cType.displayName) },
                  onClick = {
                    selectedContentType = cType
                    typeExpanded = false
                  }
                )
              }
            }
          }

          // 4. Exam Type Selector (if Question Paper)
          if (selectedContentType == ContentType.QUESTION_PAPER) {
            ExposedDropdownMenuBox(
              expanded = examExpanded,
              onExpandedChange = { examExpanded = it },
              modifier = Modifier.fillMaxWidth()
            ) {
              OutlinedTextField(
                value = selectedExamType.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Examination Paper Category *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = examExpanded) },
                modifier = Modifier
                  .menuAnchor()
                  .fillMaxWidth()
                  .testTag("upload_exam_type_dropdown")
              )
              ExposedDropdownMenu(
                expanded = examExpanded,
                onDismissRequest = { examExpanded = false }
              ) {
                listOf(
                  ExamType.MID_1,
                  ExamType.MID_2,
                  ExamType.REGULAR,
                  ExamType.SUPPLY,
                  ExamType.PREVIOUS
                ).forEach { eType ->
                  DropdownMenuItem(
                    text = { Text(eType.displayName) },
                    onClick = {
                      selectedExamType = eType
                      examExpanded = false
                    }
                  )
                }
              }
            }
          }

          // 5. Unit Number & Year row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = selectedUnitNumber,
              onValueChange = { selectedUnitNumber = it },
              label = { Text("Unit No. (1-5)") },
              singleLine = true,
              modifier = Modifier
                .weight(1f)
                .testTag("upload_unit_input")
            )
            OutlinedTextField(
              value = year,
              onValueChange = { year = it },
              label = { Text("Exam Year") },
              singleLine = true,
              modifier = Modifier
                .weight(1f)
                .testTag("upload_year_input")
            )
          }

          // 6. Topic Name
          OutlinedTextField(
            value = topicName,
            onValueChange = { topicName = it },
            label = { Text("Topic Name (Optional)") },
            placeholder = { Text("e.g. Multithreading in Java") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("upload_topic_input")
          )
        }
      }

      // Material Details Card
      OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SlateBorder),
        colors = CardDefaults.outlinedCardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Text(
            text = "Document & File Information",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900
            )
          )

          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Document Title *") },
            placeholder = { Text("e.g. Unit 1 Complete Handwritten Notes") },
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("upload_title_input")
          )

          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            placeholder = { Text("e.g. Verified by Department Faculty, includes solved numericals") },
            minLines = 2,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
          )

          // File Attachment Box
          OutlinedCard(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, if (selectedFileName != null) AccentTeal else SlateBorder),
            colors = CardDefaults.outlinedCardColors(
              containerColor = if (selectedFileName != null) SkyBlueSoft.copy(alpha = 0.3f) else EducationBackground
            ),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              if (selectedFileName != null) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = AccentTeal,
                    modifier = Modifier.size(20.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = selectedFileName!!,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = TextPrimaryDark
                    )
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                  onClick = { filePickerLauncher.launch("*/*") }
                ) {
                  Text("Replace Selected File")
                }
              } else {
                Icon(
                  imageVector = Icons.Default.CloudUpload,
                  contentDescription = null,
                  tint = RoyalBlue800,
                  modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "Attach PDF or Document File",
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                  text = "Pick actual PDF, DOCX or syllabus file from device",
                  style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                  onClick = { filePickerLauncher.launch("*/*") },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = SkyBlueSoft,
                    contentColor = RoyalBlue800
                  ),
                  modifier = Modifier.testTag("upload_pick_file_button")
                ) {
                  Icon(Icons.Default.AttachFile, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Choose File", fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          // Direct in-app Text Content editor
          OutlinedTextField(
            value = textContent,
            onValueChange = { textContent = it },
            label = { Text("Readable Notes / Important Questions Content (Optional)") },
            placeholder = { Text("You can also type or paste key notes, questions, and solutions directly for instant in-app reading...") },
            minLines = 4,
            maxLines = 8,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("upload_text_content_input")
          )
        }
      }

      // Submit Button
      Button(
        onClick = {
          val subjectName = SyllabusData.getSubjectByCode(selectedSubjectCode)?.name ?: "Diploma Subject"
          val unitInt = selectedUnitNumber.toIntOrNull()
          onSaveContent(
            existingContent?.id,
            selectedSemester,
            selectedSubjectCode,
            subjectName,
            unitInt,
            topicName.ifBlank { null },
            selectedContentType,
            selectedExamType,
            year.ifBlank { null },
            title,
            description,
            textContent,
            selectedFileUri,
            selectedFileName
          )
        },
        enabled = title.isNotBlank(),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = RoyalBlue800,
          contentColor = Color.White
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("upload_submit_button")
      ) {
        Icon(Icons.Default.CloudUpload, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = if (existingContent != null) "Update Published Material" else "Publish Official Content",
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
