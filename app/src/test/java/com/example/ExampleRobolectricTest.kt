package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SyllabusData
import com.example.data.local.AppDatabase
import com.example.data.repository.AdminAuthManager
import com.example.model.ContentType
import com.example.model.DownloadRecord
import com.example.model.EducationalContent
import com.example.model.ExamType
import com.example.model.StudyDiscussion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun testAppNameIsITLearn() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("IT Learn", appName)
  }

  @Test
  fun testOfficialSyllabusStructure() {
    // 1st & 2nd Semesters: "Syllabus content will be added soon."
    val sem1 = SyllabusData.semesters.find { it.semesterNumber == 1 }
    val sem2 = SyllabusData.semesters.find { it.semesterNumber == 2 }
    assertNotNull(sem1)
    assertNotNull(sem2)
    assertFalse(sem1!!.isContentReady)
    assertFalse(sem2!!.isContentReady)
    assertEquals("Syllabus content will be added soon.", sem1.notice)
    assertEquals("Syllabus content will be added soon.", sem2.notice)

    // 3rd Semester Subjects Check
    val sem3Subjects = SyllabusData.getSubjectsForSemester(3)
    val sem3Codes = sem3Subjects.map { it.code }
    assertTrue(sem3Codes.contains("AI-301"))
    assertTrue(sem3Codes.contains("AI-302"))
    assertTrue(sem3Codes.contains("AI-303"))
    assertTrue(sem3Codes.contains("AI-304"))
    assertTrue(sem3Codes.contains("AI-305"))
    assertTrue(sem3Codes.contains("AI-306"))
    assertTrue(sem3Codes.contains("AI-307"))
    assertTrue(sem3Codes.contains("AI-308"))
    assertTrue(sem3Codes.contains("AI-309"))

    // 4th Semester Subjects Check
    val sem4Subjects = SyllabusData.getSubjectsForSemester(4)
    val sem4Codes = sem4Subjects.map { it.code }
    assertTrue(sem4Codes.contains("AI-401"))
    assertTrue(sem4Codes.contains("AI-402"))
    assertTrue(sem4Codes.contains("AI-403"))
    assertTrue(sem4Codes.contains("AI-404"))
    assertTrue(sem4Codes.contains("AI-405"))
    assertTrue(sem4Codes.contains("AI-406"))
    assertTrue(sem4Codes.contains("AI-407"))
    assertTrue(sem4Codes.contains("AI-408"))
    assertTrue(sem4Codes.contains("AI-409"))

    // 5th Semester Subjects Check
    val sem5Subjects = SyllabusData.getSubjectsForSemester(5)
    val sem5Codes = sem5Subjects.map { it.code }
    assertTrue(sem5Codes.contains("AI-501"))
    assertTrue(sem5Codes.contains("AI-502"))
    assertTrue(sem5Codes.contains("AI-503"))
    assertTrue(sem5Codes.contains("AI-504"))
    assertTrue(sem5Codes.contains("AI-505"))
    assertTrue(sem5Codes.contains("AI-506"))
    assertTrue(sem5Codes.contains("AI-507"))
    assertTrue(sem5Codes.contains("AI-508"))
    assertTrue(sem5Codes.contains("AI-509"))

    // 6th Semester
    val sem6Subjects = SyllabusData.getSubjectsForSemester(6)
    assertEquals(1, sem6Subjects.size)
    assertEquals("AI-601", sem6Subjects[0].code)
    assertEquals("Industrial Training", sem6Subjects[0].name)
  }

  @Test
  fun testAdminAuthentication() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val adminAuth = AdminAuthManager(context)

    // Default password test
    assertTrue("Default password should authenticate", adminAuth.authenticate("c23admin"))
    assertTrue(adminAuth.isAdminLoggedIn.value)

    // Logout
    adminAuth.logout()
    assertFalse(adminAuth.isAdminLoggedIn.value)

    // Invalid passcode test
    assertFalse("Wrong passcode should be rejected", adminAuth.authenticate("wrongpass"))
    assertFalse(adminAuth.isAdminLoggedIn.value)
  }

  @Test
  fun testRoomDatabaseEducationalOperations() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = AppDatabase.getInstance(context)
    val contentDao = db.contentDao()
    val downloadDao = db.downloadDao()
    val discussionDao = db.discussionDao()

    // Insert content
    val content = EducationalContent(
      semester = 3,
      subjectCode = "AI-302",
      subjectName = "Java Programming",
      unitNumber = 1,
      topicName = "OOP Fundamentals",
      contentType = ContentType.NOTES.name,
      title = "Unit 1 Java Classes & Objects",
      description = "Comprehensive guide on class syntax and constructors",
      textContent = "Java classes define object state and behavior."
    )
    val contentId = contentDao.insertContent(content)
    assertTrue(contentId > 0)

    val fetched = contentDao.getContentById(contentId)
    assertNotNull(fetched)
    assertEquals("AI-302", fetched?.subjectCode)
    assertEquals(ContentType.NOTES.name, fetched?.contentType)

    // Test Offline Download Record
    val download = DownloadRecord(
      contentId = contentId,
      title = fetched!!.title,
      subjectCode = fetched.subjectCode,
      subjectName = fetched.subjectName,
      localPath = "/data/downloads/unit1.pdf",
      fileSizeFormatted = "1.5 MB",
      contentType = fetched.contentType,
      textContent = fetched.textContent
    )
    val dlId = downloadDao.insertDownload(download)
    assertTrue(dlId > 0)
    assertTrue(downloadDao.isDownloaded(contentId))

    // Test Discussion Record
    val discussion = StudyDiscussion(
      subjectCode = "AI-302",
      authorName = "Satwik",
      title = "Constructors in Java",
      message = "Can a constructor be declared private?"
    )
    val discId = discussionDao.insertDiscussion(discussion)
    assertTrue(discId > 0)

    val discussions = discussionDao.getDiscussionsForSubject("AI-302").first()
    assertTrue(discussions.any { it.title == "Constructors in Java" })
  }
}
