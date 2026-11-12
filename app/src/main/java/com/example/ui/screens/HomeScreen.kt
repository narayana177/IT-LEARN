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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.SemesterInfo
import com.example.ui.components.PoweredByNarayana
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.EducationBackground
import com.example.ui.theme.IceBlue
import com.example.ui.theme.RoyalBlue700
import com.example.ui.theme.RoyalBlue800
import com.example.ui.theme.RoyalBlue900
import com.example.ui.theme.SkyBlueSoft
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  isAdminLoggedIn: Boolean,
  downloadsCount: Int,
  onNavigateToSemester: (Int) -> Unit,
  onNavigateToSubject: (String) -> Unit = {},
  onNavigateToDownloads: () -> Unit,
  onNavigateToSearch: () -> Unit,
  onNavigateToGroupStudy: () -> Unit,
  onNavigateToAdminPortal: () -> Unit,
  onLogoutAdmin: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "IT Learn",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue900
              ),
              modifier = Modifier.testTag("home_app_name")
            )
            Text(
              text = "Learn • Practice • Grow",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = RoyalBlue800,
                letterSpacing = 0.5.sp
              )
            )
          }
        },
        actions = {
          IconButton(
            onClick = onNavigateToSearch,
            modifier = Modifier.testTag("home_search_action_button")
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search Syllabus and Papers",
              tint = RoyalBlue800
            )
          }

          IconButton(
            onClick = onNavigateToDownloads,
            modifier = Modifier.testTag("home_downloads_action_button")
          ) {
            if (downloadsCount > 0) {
              BadgedBox(
                badge = {
                  Badge(
                    containerColor = RoyalBlue800,
                    contentColor = Color.White
                  ) {
                    Text("$downloadsCount")
                  }
                }
              ) {
                Icon(
                  imageVector = Icons.Default.DownloadForOffline,
                  contentDescription = "Offline Downloads",
                  tint = RoyalBlue800
                )
              }
            } else {
              Icon(
                imageVector = Icons.Default.DownloadForOffline,
                contentDescription = "Offline Downloads",
                tint = RoyalBlue800
              )
            }
          }

          IconButton(
            onClick = onNavigateToAdminPortal,
            modifier = Modifier.testTag("home_admin_portal_button")
          ) {
            Icon(
              imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Default.Lock,
              contentDescription = "Admin Portal",
              tint = if (isAdminLoggedIn) AccentTeal else RoyalBlue800
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
        .testTag("home_screen_scroll"),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

      // Admin Active Mode Banner
      if (isAdminLoggedIn) {
        item {
          OutlinedCard(
            colors = CardDefaults.outlinedCardColors(
              containerColor = IceBlue
            ),
            border = BorderStroke(1.dp, AccentTeal),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("admin_active_banner")
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  imageVector = Icons.Default.VerifiedUser,
                  contentDescription = null,
                  tint = AccentTeal,
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "Admin Mode Active",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = RoyalBlue900
                    )
                  )
                  Text(
                    text = "Authorized to upload, edit & manage C-23 content",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                  )
                }
              }

              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(
                  onClick = onNavigateToAdminPortal,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(text = "Manage", fontSize = 12.sp)
                }
                OutlinedButton(
                  onClick = onLogoutAdmin,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(text = "Exit", fontSize = 12.sp)
                }
              }
            }
          }
        }
      }

      // Hero Card: Diploma C-23 Academic Hub
      item {
        ElevatedCard(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.elevatedCardColors(
            containerColor = RoyalBlue800
          ),
          elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("home_hero_banner")
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(
                Brush.horizontalGradient(
                  colors = listOf(
                    RoyalBlue900,
                    RoyalBlue800,
                    RoyalBlue700
                  )
                )
              )
              .padding(22.dp)
          ) {
            Column {
              Surface(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = "Diploma C-23 Curriculum",
                  style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  ),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Text(
                text = "Welcome to IT Learn",
                style = MaterialTheme.typography.headlineSmall.copy(
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White
                )
              )

              Spacer(modifier = Modifier.height(6.dp))

              Text(
                text = "Direct access to syllabus, unit topics, study materials, notes, and question papers for all semesters.",
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = Color.White.copy(alpha = 0.9f),
                  lineHeight = 20.sp
                )
              )

              Spacer(modifier = Modifier.height(18.dp))

              // Quick Action Shortcuts inside Hero
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Button(
                  onClick = { onNavigateToSemester(3) },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = RoyalBlue900
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Text(text = "3rd Sem", fontWeight = FontWeight.Bold)
                }

                Button(
                  onClick = { onNavigateToSemester(4) },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.25f),
                    contentColor = Color.White
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Text(text = "4th Sem", fontWeight = FontWeight.Bold)
                }

                Button(
                  onClick = { onNavigateToSemester(5) },
                  colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.25f),
                    contentColor = Color.White
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.weight(1f)
                ) {
                  Text(text = "5th Sem", fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // Feature Quick Access Strip
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          QuickFeatureCard(
            title = "Downloads",
            subtitle = "$downloadsCount files offline",
            icon = Icons.Default.DownloadDone,
            onClick = onNavigateToDownloads,
            modifier = Modifier.weight(1f)
          )

          QuickFeatureCard(
            title = "Group Study",
            subtitle = "Peer discussions",
            icon = Icons.Default.Group,
            onClick = onNavigateToGroupStudy,
            modifier = Modifier.weight(1f)
          )

          QuickFeatureCard(
            title = "Search",
            subtitle = "Syllabus & papers",
            icon = Icons.Default.Search,
            onClick = onNavigateToSearch,
            modifier = Modifier.weight(1f)
          )
        }
      }

      // Section Header: Semesters
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Select Semester",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = RoyalBlue900
            )
          )

          Text(
            text = "1st to 6th Semester",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )
        }
      }

      // Semester Cards (1 to 6)
      items(SyllabusData.semesters.size) { index ->
        val sem = SyllabusData.semesters[index]
        SemesterCard(
          semester = sem,
          onClick = { onNavigateToSemester(sem.semesterNumber) },
          onSubjectClick = onNavigateToSubject
        )
      }

      // Subtly placed footer credit at the bottom of the Home screen
      item {
        Spacer(modifier = Modifier.height(10.dp))
        PoweredByNarayana(isSplash = false)
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
private fun QuickFeatureCard(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  OutlinedCard(
    modifier = modifier
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag("quick_feature_${title.lowercase()}"),
    shape = RoundedCornerShape(14.dp),
    border = BorderStroke(1.dp, SlateBorder),
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .background(SkyBlueSoft, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = RoyalBlue800,
          modifier = Modifier.size(18.dp)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
          fontWeight = FontWeight.Bold,
          color = TextPrimaryDark
        )
      )

      Text(
        text = subtitle,
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextMuted,
          fontSize = 10.sp
        ),
        maxLines = 1
      )
    }
  }
}

@Composable
private fun SemesterCard(
  semester: SemesterInfo,
  onClick: () -> Unit,
  onSubjectClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val subjects = SyllabusData.getSubjectsForSemester(semester.semesterNumber)
  val theorySubjects = subjects.filter { !it.isPractical }
  val practicalSubjects = subjects.filter { it.isPractical }

  OutlinedCard(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .testTag("semester_card_${semester.semesterNumber}"),
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, SlateBorder),
    colors = CardDefaults.outlinedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Semester Number Badge
        Box(
          modifier = Modifier
            .size(46.dp)
            .background(
              if (semester.isContentReady) SkyBlueSoft else SlateBorder.copy(alpha = 0.5f),
              CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${semester.semesterNumber}",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              color = if (semester.isContentReady) RoyalBlue800 else TextMuted
            )
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = semester.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
              )
            )

            if (!semester.isContentReady) {
              Surface(
                color = AccentAmber.copy(alpha = 0.14f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "Notice",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AccentAmber
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            } else {
              Surface(
                color = SkyBlueSoft,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "${subjects.size} Subjects",
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = RoyalBlue800
                  ),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = if (semester.isContentReady) {
              if (semester.semesterNumber == 6) "Industrial Internship" else "${theorySubjects.size} Theory • ${practicalSubjects.size} Practical Labs"
            } else {
              "Official C-23 Curriculum"
            },
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
          )
        }

        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
          contentDescription = "Open ${semester.title}",
          tint = TextMuted,
          modifier = Modifier.size(16.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      if (!semester.isContentReady) {
        // 1st and 2nd Semesters: MUST display 'Syllabus content will be added soon.'
        Surface(
          color = SkyBlueSoft.copy(alpha = 0.45f),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Schedule,
              contentDescription = null,
              tint = RoyalBlue800,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Syllabus content will be added soon.",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = RoyalBlue900
                )
              )
              Text(
                text = "Curriculum will be updated as notified by the technical board.",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
              )
            }
          }
        }
      } else {
        // 3rd, 4th, 5th, 6th Semesters: Show the specific subject names!
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          if (theorySubjects.isNotEmpty()) {
            Text(
              text = "Theory Subjects",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = RoyalBlue800
              )
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              theorySubjects.forEach { subj ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onSubjectClick(subj.code) }
                    .padding(vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    color = SkyBlueSoft,
                    shape = RoundedCornerShape(4.dp)
                  ) {
                    Text(
                      text = subj.code,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue800
                      ),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = subj.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = FontWeight.Medium,
                      color = TextPrimaryDark
                    ),
                    maxLines = 1
                  )
                }
              }
            }
          }

          if (practicalSubjects.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (semester.semesterNumber == 6) "Curriculum Program" else "Practicals & Labs",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = AccentTeal
              )
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              practicalSubjects.forEach { subj ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onSubjectClick(subj.code) }
                    .padding(vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    color = AccentTeal.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                  ) {
                    Text(
                      text = subj.code,
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal
                      ),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = subj.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = FontWeight.Medium,
                      color = TextPrimaryDark
                    ),
                    maxLines = 1
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
