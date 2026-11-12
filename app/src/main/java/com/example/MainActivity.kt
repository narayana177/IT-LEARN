package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.model.EducationalContent
import com.example.ui.screens.AdminPortalScreen
import com.example.ui.screens.AdminUploadScreen
import com.example.ui.screens.DocumentReaderScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.GroupStudyScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SemesterScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SubjectDetailScreen
import com.example.ui.screens.UnitTopicDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val currentScreen by viewModel.currentScreen.collectAsState()
        val allContent by viewModel.allContent.collectAsState()
        val downloads by viewModel.allDownloads.collectAsState()
        val downloadedContentIds = remember(downloads) { downloads.map { it.contentId }.toSet() }
        val downloadingProgress by viewModel.downloadingProgress.collectAsState()
        val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
        val userMessage by viewModel.userMessage.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(userMessage) {
          userMessage?.let { msg ->
            snackbarHostState.showSnackbar(
              message = msg,
              duration = SnackbarDuration.Short
            )
            viewModel.dismissUserMessage()
          }
        }

        // Seamless BackHandler navigation
        BackHandler(enabled = currentScreen !is Screen.Splash && currentScreen !is Screen.Home) {
          viewModel.navigateBack()
        }

        Scaffold(
          snackbarHost = { SnackbarHost(snackbarHostState) },
          modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            AnimatedContent(
              targetState = currentScreen,
              transitionSpec = { fadeIn() togetherWith fadeOut() },
              label = "ScreenTransition"
            ) { screen ->
              when (screen) {
                is Screen.Splash -> {
                  SplashScreen(
                    onNavigateToHome = { viewModel.navigateTo(Screen.Home) }
                  )
                }

                is Screen.Home -> {
                  HomeScreen(
                    isAdminLoggedIn = isAdminLoggedIn,
                    downloadsCount = downloads.size,
                    onNavigateToSemester = { semNum ->
                      viewModel.navigateTo(Screen.SemesterView(semNum))
                    },
                    onNavigateToSubject = { subjectCode ->
                      viewModel.navigateTo(Screen.SubjectDetail(subjectCode))
                    },
                    onNavigateToDownloads = {
                      viewModel.navigateTo(Screen.Downloads)
                    },
                    onNavigateToSearch = {
                      viewModel.navigateTo(Screen.Search)
                    },
                    onNavigateToGroupStudy = {
                      viewModel.navigateTo(Screen.GroupStudy())
                    },
                    onNavigateToAdminPortal = {
                      viewModel.navigateTo(Screen.AdminPortal)
                    },
                    onLogoutAdmin = {
                      viewModel.logoutAdmin()
                    }
                  )
                }

                is Screen.SemesterView -> {
                  SemesterScreen(
                    semesterNumber = screen.semesterNumber,
                    onNavigateBack = { viewModel.navigateBack() },
                    onNavigateToSubject = { subjectCode ->
                      viewModel.navigateTo(Screen.SubjectDetail(subjectCode))
                    }
                  )
                }

                is Screen.SubjectDetail -> {
                  SubjectDetailScreen(
                    subjectCode = screen.subjectCode,
                    initialTab = screen.initialTab,
                    allContent = allContent,
                    downloadedContentIds = downloadedContentIds,
                    downloadingProgress = downloadingProgress,
                    isAdminLoggedIn = isAdminLoggedIn,
                    onNavigateBack = { viewModel.navigateBack() },
                    onViewContent = { content ->
                      viewModel.navigateTo(
                        Screen.DocumentReader(
                          title = content.title,
                          subtitle = "${content.subjectCode} - ${content.subjectName}",
                          filePath = content.filePath,
                          textContent = content.textContent,
                          contentId = content.id
                        )
                      )
                    },
                    onDownloadContent = { content ->
                      viewModel.downloadFile(content)
                    },
                    onEditContent = if (isAdminLoggedIn) {
                      { content ->
                        viewModel.navigateTo(
                          Screen.AdminUpload(editContentId = content.id, prefillSubjectCode = content.subjectCode)
                        )
                      }
                    } else null,
                    onDeleteContent = if (isAdminLoggedIn) {
                      { contentId ->
                        viewModel.deleteAdminContent(contentId)
                      }
                    } else null,
                    onAdminUploadClick = { code ->
                      viewModel.navigateTo(Screen.AdminUpload(prefillSubjectCode = code))
                    },
                    onTopicClick = { subj, unit ->
                      viewModel.navigateTo(Screen.UnitTopicDetail(subj.code, unit.unitNumber))
                    }
                  )
                }

                is Screen.UnitTopicDetail -> {
                  UnitTopicDetailScreen(
                    subjectCode = screen.subjectCode,
                    unitNumber = screen.unitNumber,
                    allContent = allContent,
                    downloadedContentIds = downloadedContentIds,
                    downloadingProgress = downloadingProgress,
                    isAdminLoggedIn = isAdminLoggedIn,
                    onNavigateBack = { viewModel.navigateBack() },
                    onViewContent = { content ->
                      viewModel.navigateTo(
                        Screen.DocumentReader(
                          title = content.title,
                          subtitle = "${content.subjectCode} - Unit ${screen.unitNumber}",
                          filePath = content.filePath,
                          textContent = content.textContent,
                          contentId = content.id
                        )
                      )
                    },
                    onDownloadContent = { content ->
                      viewModel.downloadFile(content)
                    }
                  )
                }

                is Screen.Downloads -> {
                  DownloadsScreen(
                    downloads = downloads,
                    onNavigateBack = { viewModel.navigateBack() },
                    onOpenDownload = { record ->
                      viewModel.navigateTo(
                        Screen.DocumentReader(
                          title = record.title,
                          subtitle = "${record.subjectCode} - ${record.subjectName}",
                          filePath = record.localPath,
                          textContent = record.textContent,
                          contentId = record.contentId
                        )
                      )
                    },
                    onDeleteDownload = { downloadId ->
                      viewModel.deleteDownload(downloadId)
                    },
                    onBrowseSubjects = {
                      viewModel.navigateTo(Screen.SemesterView(3))
                    }
                  )
                }

                is Screen.Search -> {
                  SearchScreen(
                    viewModel = viewModel,
                    onNavigateBack = { viewModel.navigateBack() },
                    onNavigateToSubject = { code ->
                      viewModel.navigateTo(Screen.SubjectDetail(code))
                    },
                    onViewContent = { content ->
                      viewModel.navigateTo(
                        Screen.DocumentReader(
                          title = content.title,
                          subtitle = "${content.subjectCode} - ${content.subjectName}",
                          filePath = content.filePath,
                          textContent = content.textContent,
                          contentId = content.id
                        )
                      )
                    },
                    onDownloadContent = { content ->
                      viewModel.downloadFile(content)
                    }
                  )
                }

                is Screen.GroupStudy -> {
                  GroupStudyScreen(
                    viewModel = viewModel,
                    initialSubjectCode = screen.subjectCode,
                    onNavigateBack = { viewModel.navigateBack() }
                  )
                }

                is Screen.AdminPortal -> {
                  AdminPortalScreen(
                    viewModel = viewModel,
                    onNavigateBack = { viewModel.navigateBack() },
                    onNavigateToUpload = { prefillCode ->
                      viewModel.navigateTo(Screen.AdminUpload(prefillSubjectCode = prefillCode))
                    },
                    onEditContent = { content ->
                      viewModel.navigateTo(
                        Screen.AdminUpload(editContentId = content.id, prefillSubjectCode = content.subjectCode)
                      )
                    },
                    onViewContent = { content ->
                      viewModel.navigateTo(
                        Screen.DocumentReader(
                          title = content.title,
                          subtitle = "${content.subjectCode} - ${content.subjectName}",
                          filePath = content.filePath,
                          textContent = content.textContent,
                          contentId = content.id
                        )
                      )
                    }
                  )
                }

                is Screen.AdminUpload -> {
                  AdminUploadScreen(
                    editContentId = screen.editContentId,
                    prefillSubjectCode = screen.prefillSubjectCode,
                    allContent = allContent,
                    onNavigateBack = { viewModel.navigateBack() },
                    onSaveContent = { existingId, sem, code, name, unitNum, topic, cType, eType, yr, title, desc, text, uri, fName ->
                      viewModel.saveUploadedContent(
                        existingId = existingId,
                        semester = sem,
                        subjectCode = code,
                        subjectName = name,
                        unitNumber = unitNum,
                        topicName = topic,
                        contentType = cType,
                        examType = eType,
                        year = yr,
                        title = title,
                        description = desc,
                        textContent = text,
                        fileUri = uri,
                        fileName = fName
                      )
                    }
                  )
                }

                is Screen.DocumentReader -> {
                  DocumentReaderScreen(
                    title = screen.title,
                    subtitle = screen.subtitle,
                    filePath = screen.filePath,
                    textContent = screen.textContent,
                    onBackClick = { viewModel.navigateBack() }
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
