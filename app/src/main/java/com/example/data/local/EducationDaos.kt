package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.DownloadRecord
import com.example.model.EducationalContent
import com.example.model.StudyDiscussion
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
  @Query("SELECT * FROM educational_content ORDER BY uploadedAtMillis DESC")
  fun getAllContent(): Flow<List<EducationalContent>>

  @Query("SELECT * FROM educational_content WHERE subjectCode = :subjectCode ORDER BY uploadedAtMillis DESC")
  fun getContentBySubject(subjectCode: String): Flow<List<EducationalContent>>

  @Query("SELECT * FROM educational_content WHERE subjectCode = :subjectCode AND contentType = :contentType ORDER BY uploadedAtMillis DESC")
  fun getContentBySubjectAndType(subjectCode: String, contentType: String): Flow<List<EducationalContent>>

  @Query("SELECT * FROM educational_content WHERE semester = :semester ORDER BY uploadedAtMillis DESC")
  fun getContentBySemester(semester: Int): Flow<List<EducationalContent>>

  @Query("SELECT * FROM educational_content WHERE id = :id LIMIT 1")
  suspend fun getContentById(id: Long): EducationalContent?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertContent(content: EducationalContent): Long

  @Update
  suspend fun updateContent(content: EducationalContent)

  @Delete
  suspend fun deleteContent(content: EducationalContent)

  @Query("DELETE FROM educational_content WHERE id = :id")
  suspend fun deleteContentById(id: Long)

  @Query("""
    SELECT * FROM educational_content 
    WHERE title LIKE '%' || :query || '%' 
       OR description LIKE '%' || :query || '%' 
       OR subjectName LIKE '%' || :query || '%' 
       OR subjectCode LIKE '%' || :query || '%' 
       OR topicName LIKE '%' || :query || '%'
    ORDER BY uploadedAtMillis DESC
  """)
  fun searchContent(query: String): Flow<List<EducationalContent>>
}

@Dao
interface DownloadDao {
  @Query("SELECT * FROM download_records ORDER BY downloadedAtMillis DESC")
  fun getAllDownloads(): Flow<List<DownloadRecord>>

  @Query("SELECT COUNT(*) > 0 FROM download_records WHERE contentId = :contentId")
  suspend fun isDownloaded(contentId: Long): Boolean

  @Query("SELECT * FROM download_records WHERE contentId = :contentId LIMIT 1")
  suspend fun getDownloadByContentId(contentId: Long): DownloadRecord?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDownload(download: DownloadRecord): Long

  @Query("DELETE FROM download_records WHERE id = :id")
  suspend fun deleteDownloadById(id: Long)

  @Query("DELETE FROM download_records WHERE contentId = :contentId")
  suspend fun deleteDownloadByContentId(contentId: Long)
}

@Dao
interface DiscussionDao {
  @Query("SELECT * FROM study_discussions WHERE subjectCode = :subjectCode ORDER BY timestamp DESC")
  fun getDiscussionsForSubject(subjectCode: String): Flow<List<StudyDiscussion>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDiscussion(discussion: StudyDiscussion): Long

  @Query("DELETE FROM study_discussions WHERE id = :id")
  suspend fun deleteDiscussion(id: Long)
}
