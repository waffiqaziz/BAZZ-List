package com.waffiq.bazz_list.di.module

import android.content.Context
import com.waffiq.bazz_list.di.providers.NoteUseCaseProvider
import com.waffiq.bazz_list.domain.usecase.note.NotesUseCase

object Injection {
  fun provideGetAllNoteUseCase(context: Context): NotesUseCase =
    NoteUseCaseProvider.provideGetALlNote(context)
}