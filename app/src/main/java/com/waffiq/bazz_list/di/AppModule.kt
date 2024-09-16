package com.waffiq.bazz_list.di

import com.waffiq.bazz_list.core.domain.usecase.note.NotesInteractor
import com.waffiq.bazz_list.core.domain.usecase.note.NotesUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

  @Binds
  abstract fun provideNotesUsecase(notesInteractor: NotesInteractor): NotesUseCase

}