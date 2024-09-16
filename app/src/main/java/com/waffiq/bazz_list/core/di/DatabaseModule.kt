package com.waffiq.bazz_list.core.di

import android.content.Context
import androidx.room.Room
import com.waffiq.bazz_list.core.data.local.room.NoteDao
import com.waffiq.bazz_list.core.data.local.room.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

  @Singleton
  @Provides
  fun provideDatabase(context: Context): NoteDatabase =  Room.databaseBuilder(
    context,
    NoteDatabase::class.java, "note.db"
  ).fallbackToDestructiveMigration().build()

  @Provides
  fun provideNotesDao(database: NoteDatabase): NoteDao = database.noteDao()
}