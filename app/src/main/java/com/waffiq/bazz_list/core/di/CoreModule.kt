package com.waffiq.bazz_list.core.di

import androidx.room.Room
import com.waffiq.bazz_list.core.data.local.datasource.LocalDataSource
import com.waffiq.bazz_list.core.data.local.room.NoteDatabase
import com.waffiq.bazz_list.core.data.repository.NoteRepository
import com.waffiq.bazz_list.core.domain.repository.INoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
  factory { get<NoteDatabase>().noteDao() }
  single {
    Room.databaseBuilder(
      androidContext(),
      NoteDatabase::class.java, "note.db"
    ).fallbackToDestructiveMigration().build()
  }
}

val repositoryModule = module {
  single { LocalDataSource(get()) }
  single<INoteRepository> { NoteRepository(get()) }
}