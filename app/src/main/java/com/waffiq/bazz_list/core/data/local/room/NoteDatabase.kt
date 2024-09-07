package com.waffiq.bazz_list.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waffiq.bazz_list.core.data.local.model.NoteEntity

@Database(
  entities = [NoteEntity::class],
  version = 1,
  exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
  abstract fun noteDao(): NoteDao
}