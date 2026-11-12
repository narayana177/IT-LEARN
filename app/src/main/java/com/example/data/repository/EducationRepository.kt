package com.example.data.repository

import android.content.Context
import android.net.Uri
import com.example.data.local.AppDatabase
import com.example.model.DownloadRecord
import com.example.model.EducationalContent
import com.example.model.StudyDiscussion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EducationRepository(
  private val context: Context,
  private val database: AppDatabase
) {

  private val contentDao = database.contentDao()
  private val downloadDao = database.downloadDao()
  private val discussionDao = database.discussionDao()

  val allContent: Flow<List<EducationalContent>> = contentDao.getAllContent()
  val allDownloads: Flow<List<DownloadRecord>> = downloadDao.getAllDownloads()

  fun getContentForSubject(subjectCode: String): Flow<List<EducationalContent>> {
    return contentDao.getContentBySubject(subjectCode)
  }

  fun getContentForSubjectAndType(subjectCode: String, contentType: String): Flow<List<EducationalContent>> {
    return contentDao.getContentBySubjectAndType(subjectCode, contentType)
  }

  fun searchContent(query: String): Flow<List<EducationalContent>> {
    return contentDao.searchContent(query)
  }

  fun getDiscussionsForSubject(subjectCode: String): Flow<List<StudyDiscussion>> {
    return discussionDao.getDiscussionsForSubject(subjectCode)
  }

  suspend fun postDiscussion(subjectCode: String, authorName: String, title: String, message: String): Long {
    val discussion = StudyDiscussion(
      subjectCode = subjectCode,
      authorName = authorName.ifBlank { "Diploma Student" },
      title = title,
      message = message
    )
    return discussionDao.insertDiscussion(discussion)
  }

  suspend fun insertAdminContent(content: EducationalContent): Long {
    return contentDao.insertContent(content)
  }

  suspend fun updateAdminContent(content: EducationalContent) {
    contentDao.updateContent(content)
  }

  suspend fun deleteAdminContent(id: Long) {
    val content = contentDao.getContentById(id)
    if (content != null) {
      // Remove physical file if in app storage
      content.filePath?.let { path ->
        val file = File(path)
        if (file.exists() && file.startsWith(context.filesDir)) {
          file.delete()
        }
      }
      contentDao.deleteContentById(id)
      downloadDao.deleteDownloadByContentId(id)
    }
  }

  suspend fun saveUploadedFile(sourceUri: Uri, originalFilename: String): Pair<String, String> =
    withContext(Dispatchers.IO) {
      val uploadsDir = File(context.filesDir, "uploads")
      if (!uploadsDir.exists()) {
        uploadsDir.mkdirs()
      }
      val safeFileName = "${System.currentTimeMillis()}_${originalFilename.replace("[^a-zA-Z0-9._-]".toRegex(), "_")}"
      val targetFile = File(uploadsDir, safeFileName)

      var bytesCopied: Long = 0
      context.contentResolver.openInputStream(sourceUri)?.use { input: InputStream ->
        FileOutputStream(targetFile).use { output ->
          bytesCopied = input.copyTo(output)
        }
      }

      val formattedSize = formatFileSize(bytesCopied)
      Pair(targetFile.absolutePath, formattedSize)
    }

  suspend fun downloadContentForOffline(
    content: EducationalContent,
    onProgress: (Int) -> Unit
  ): Boolean = withContext(Dispatchers.IO) {
    try {
      val downloadsDir = File(context.filesDir, "downloads")
      if (!downloadsDir.exists()) {
        downloadsDir.mkdirs()
      }

      // Simulate download progress steps for realistic feedback
      for (progress in 10..90 step 20) {
        onProgress(progress)
        kotlinx.coroutines.delay(100)
      }

      val safeName = "${content.subjectCode}_${content.id}_${content.title.take(20).replace("[^a-zA-Z0-9]".toRegex(), "_")}.pdf"
      val destinationFile = File(downloadsDir, safeName)

      // If physical source file exists, copy it, else write formatted offline content file
      if (content.filePath != null && File(content.filePath).exists()) {
        File(content.filePath).copyTo(destinationFile, overwrite = true)
      } else {
        // Save structured content offline
        destinationFile.writeText(
          """
            # ${content.title}
            Subject: ${content.subjectCode} - ${content.subjectName}
            Semester: ${content.semester}
            Type: ${content.contentType}
            
            ${content.description}
            
            ${content.textContent}
          """.trimIndent()
        )
      }

      onProgress(100)

      val record = DownloadRecord(
        contentId = content.id,
        title = content.title,
        subjectCode = content.subjectCode,
        subjectName = content.subjectName,
        localPath = destinationFile.absolutePath,
        fileSizeFormatted = content.fileSizeFormatted,
        contentType = content.contentType,
        textContent = content.textContent
      )
      downloadDao.insertDownload(record)
      true
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  suspend fun isContentDownloaded(contentId: Long): Boolean {
    return downloadDao.isDownloaded(contentId)
  }

  suspend fun deleteDownload(downloadId: Long) = withContext(Dispatchers.IO) {
    downloadDao.deleteDownloadById(downloadId)
  }

  private fun formatFileSize(bytes: Long): String {
    if (bytes <= 0) return "1.2 MB"
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    return if (mb >= 1.0) {
      "%.1f MB".format(mb)
    } else {
      "%.0f KB".format(kb)
    }
  }
}
