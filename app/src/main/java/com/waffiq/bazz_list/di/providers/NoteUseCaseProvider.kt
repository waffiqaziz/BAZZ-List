package com.waffiq.bazz_list.di.providers

import android.content.Context
import com.waffiq.bazz_list.data.local.datasource.LocalDataSource
import com.waffiq.bazz_list.data.local.room.NoteDatabase
import com.waffiq.bazz_list.data.repository.NoteRepository
import com.waffiq.bazz_list.domain.usecase.note.NotesInteractor
import com.waffiq.bazz_list.domain.usecase.note.NotesUseCase

object NoteUseCaseProvider {
  private fun provideNoteRepository(context: Context): NoteRepository {
    val noteDatabase = NoteDatabase.getInstance(context)
    val localDataSource = LocalDataSource.getInstance(noteDatabase.noteDao())
    return NoteRepository(localDataSource)
  }

  fun provideGetALlNote(context: Context): NotesUseCase {
    val repository = provideNoteRepository(context)
    return NotesInteractor(repository)
  }
}