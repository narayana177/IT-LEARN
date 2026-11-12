package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "educational_content")
data class EducationalContent(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val semester: Int,
  val subjectCode: String,
  val subjectName: String,
  val unitNumber: Int? = null,
  val topicName: String? = null,
  val contentType: String, // from ContentType.name
  val examType: String = ExamType.NONE.name, // from ExamType.name
  val year: String? = null,
  val title: String,
  val description: String = "",
  val filePath: String? = null,
  val fileSizeFormatted: String = "1.2 MB",
  val uploadedAtMillis: Long = System.currentTimeMillis(),
  val uploadedByAdmin: String = "Official Admin",
  val textContent: String = "" // In-app readable notes/question content
)

@Entity(tableName = "download_records")
data class DownloadRecord(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val contentId: Long,
  val title: String,
  val subjectCode: String,
  val subjectName: String,
  val localPath: String,
  val fileSizeFormatted: String,
  val downloadedAtMillis: Long = System.currentTimeMillis(),
  val contentType: String,
  val textContent: String = ""
)

@Entity(tableName = "study_discussions")
data class StudyDiscussion(
  @PrimaryKey(autoGenerate = true)
  val id: Long = 0,
  val subjectCode: String,
  val authorName: String,
  val title: String,
  val message: String,
  val timestamp: Long = System.currentTimeMillis(),
  val repliesCount: Int = 0
)
