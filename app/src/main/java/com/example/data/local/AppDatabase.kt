package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.model.DownloadRecord
import com.example.model.EducationalContent
import com.example.model.StudyDiscussion

@Database(
  entities = [
    EducationalContent::class,
    DownloadRecord::class,
    StudyDiscussion::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun contentDao(): ContentDao
  abstract fun downloadDao(): DownloadDao
  abstract fun discussionDao(): DiscussionDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "it_learn_database"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
