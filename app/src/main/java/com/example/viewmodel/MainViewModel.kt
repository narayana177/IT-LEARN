package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SyllabusData
import com.example.data.local.AppDatabase
import com.example.data.repository.AdminAuthManager
import com.example.data.repository.EducationRepository
import com.example.model.ContentType
import com.example.model.DownloadRecord
import com.example.model.EducationalContent
import com.example.model.ExamType
import com.example.model.StudyDiscussion
import com.example.model.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface Screen {
  object Splash : Screen
  object Home : Screen
  data class SemesterView(val semesterNumber: Int) : Screen
  data class SubjectDetail(val subjectCode: String, val initialTab: Int = 0) : Screen
  data class UnitTopicDetail(val subjectCode: String, val unitNumber: Int) : Screen
  object Downloads : Screen
  object Search : Screen
  data class GroupStudy(val subjectCode: String? = null) : Screen
  object AdminPortal : Screen
  data class AdminUpload(val editContentId: Long? = null, val prefillSubjectCode: String? = null) : Screen
  data class DocumentReader(
    val title: String,
    val subtitle: String,
    val filePath: String?,
    val textContent: String,
    val contentId: Long? = null
  ) : Screen
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

  private val database = AppDatabase.getInstance(application)
  private val repository = EducationRepository(application, database)
  val adminAuthManager = AdminAuthManager(application)

  private val _navigationStack = MutableStateFlow<List<Screen>>(listOf(Screen.Splash))
  val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.Splash).apply {
    viewModelScope.launch {
      _navigationStack.collect { stack ->
        value = stack.lastOrNull() ?: Screen.Home
      }
    }
  }

  val isAdminLoggedIn: StateFlow<Boolean> = adminAuthManager.isAdminLoggedIn

  val allContent: StateFlow<List<EducationalContent>> = repository.allContent
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allDownloads: StateFlow<List<DownloadRecord>> = repository.allDownloads
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Search state
  val searchQuery = MutableStateFlow("")
  val searchSemesterFilter = MutableStateFlow<Int?>(null)
  val searchCategoryFilter = MutableStateFlow<String?>(null)

  val filteredSearchResults: StateFlow<List<EducationalContent>> = combine(
    allContent,
    searchQuery,
    searchSemesterFilter,
    searchCategoryFilter
  ) { contentList, query, semFilter, catFilter ->
    contentList.filter { item ->
      val matchesQuery = query.isBlank() ||
        item.title.contains(query, ignoreCase = true) ||
        item.subjectCode.contains(query, ignoreCase = true) ||
        item.subjectName.contains(query, ignoreCase = true) ||
        (item.topicName?.contains(query, ignoreCase = true) == true) ||
        item.description.contains(query, ignoreCase = true)

      val matchesSem = semFilter == null || item.semester == semFilter
      val matchesCat = catFilter == null || item.contentType.equals(catFilter, ignoreCase = true)

      matchesQuery && matchesSem && matchesCat
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Download progress mapping contentId -> percentage
  private val _downloadingProgress = MutableStateFlow<Map<Long, Int>>(emptyMap())
  val downloadingProgress: StateFlow<Map<Long, Int>> = _downloadingProgress.asStateFlow()

  // User feedback toast/message
  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  fun navigateTo(screen: Screen) {
    val current = _navigationStack.value
    if (screen is Screen.Splash) {
      _navigationStack.value = listOf(Screen.Splash)
    } else if (screen is Screen.Home) {
      _navigationStack.value = listOf(Screen.Home)
    } else {
      _navigationStack.value = current + screen
    }
  }

  fun navigateBack(): Boolean {
    val current = _navigationStack.value
    return if (current.size > 1) {
      _navigationStack.value = current.dropLast(1)
      true
    } else {
      false
    }
  }

  fun dismissUserMessage() {
    _userMessage.value = null
  }

  fun showMessage(msg: String) {
    _userMessage.value = msg
  }

  fun authenticateAdmin(passcode: String): Boolean {
    val success = adminAuthManager.authenticate(passcode)
    if (success) {
      _userMessage.value = "Admin authentication successful. Management mode unlocked."
    } else {
      _userMessage.value = "Invalid Admin passcode. Access denied."
    }
    return success
  }

  fun logoutAdmin() {
    adminAuthManager.logout()
    _userMessage.value = "Admin logged out. Student view restored."
    if (currentScreen.value is Screen.AdminPortal || currentScreen.value is Screen.AdminUpload) {
      navigateTo(Screen.Home)
    }
  }

  fun updateAdminPasscode(oldPass: String, newPass: String): Boolean {
    val success = adminAuthManager.updatePasscode(oldPass, newPass)
    if (success) {
      _userMessage.value = "Admin passcode updated successfully."
    } else {
      _userMessage.value = "Failed to update passcode. Verify old passcode (min 6 chars)."
    }
    return success
  }

  fun downloadFile(content: EducationalContent) {
    viewModelScope.launch {
      _downloadingProgress.value = _downloadingProgress.value + (content.id to 0)
      val success = repository.downloadContentForOffline(content) { progress ->
        _downloadingProgress.value = _downloadingProgress.value + (content.id to progress)
      }
      _downloadingProgress.value = _downloadingProgress.value - content.id
      if (success) {
        _userMessage.value = "Downloaded '${content.title}' for offline reading."
      } else {
        _userMessage.value = "Download failed. Please try again."
      }
    }
  }

  fun deleteDownload(downloadId: Long) {
    viewModelScope.launch {
      repository.deleteDownload(downloadId)
      _userMessage.value = "Removed download from offline storage."
    }
  }

  fun deleteAdminContent(id: Long) {
    if (!isAdminLoggedIn.value) return
    viewModelScope.launch {
      repository.deleteAdminContent(id)
      _userMessage.value = "Educational content deleted successfully."
    }
  }

  fun saveUploadedContent(
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
  ) {
    if (!isAdminLoggedIn.value) {
      _userMessage.value = "Unauthorized: Only Admin can upload content."
      return
    }

    viewModelScope.launch {
      var savedFilePath: String? = null
      var formattedSize = "1.5 MB"

      if (fileUri != null && fileName != null) {
        val result = repository.saveUploadedFile(fileUri, fileName)
        savedFilePath = result.first
        formattedSize = result.second
      }

      val content = EducationalContent(
        id = existingId ?: 0,
        semester = semester,
        subjectCode = subjectCode,
        subjectName = subjectName,
        unitNumber = unitNumber,
        topicName = topicName,
        contentType = contentType.name,
        examType = if (contentType == ContentType.QUESTION_PAPER) examType.name else ExamType.NONE.name,
        year = year,
        title = title,
        description = description,
        filePath = savedFilePath,
        fileSizeFormatted = formattedSize,
        textContent = textContent,
        uploadedAtMillis = System.currentTimeMillis(),
        uploadedByAdmin = "Authorized C-23 Admin"
      )

      if (existingId == null || existingId == 0L) {
        repository.insertAdminContent(content)
        _userMessage.value = "Successfully published '$title' to $subjectCode."
      } else {
        repository.updateAdminContent(content)
        _userMessage.value = "Successfully updated '$title'."
      }

      navigateTo(Screen.AdminPortal)
    }
  }

  fun postDiscussionMessage(subjectCode: String, authorName: String, title: String, message: String) {
    viewModelScope.launch {
      repository.postDiscussion(subjectCode, authorName, title, message)
      _userMessage.value = "Discussion query posted in $subjectCode."
    }
  }

  fun getDiscussionsForSubject(subjectCode: String) =
    repository.getDiscussionsForSubject(subjectCode)
}
