package com.waffiq.bazz_list.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.waffiq.bazz_list.data.local.model.NoteEntity

@Database(
  entities = [NoteEntity::class],
  version = 1,
  exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

  abstract fun noteDao(): NoteDao

  companion object {
    @Volatile
    private var INSTANCE: NoteDatabase? = null

    @JvmStatic
    fun getInstance(context: Context): NoteDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(
          context.applicationContext,
          NoteDatabase::class.java, "note.db"
        )
          .fallbackToDestructiveMigration()
          .build()
          .also { INSTANCE = it }
      }
    }
  }
}