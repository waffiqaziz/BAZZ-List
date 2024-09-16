package com.waffiq.bazz_list.core.di

import com.waffiq.bazz_list.core.data.repository.NoteRepository
import com.waffiq.bazz_list.core.domain.repository.INoteRepository
import dagger.Binds
import dagger.Module

@Module(includes = [DatabaseModule::class])
abstract class RepositoryModule {

  @Binds
  abstract fun provideRepository(noteRepository: NoteRepository): INoteRepository
}