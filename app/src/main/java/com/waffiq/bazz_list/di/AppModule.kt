package com.waffiq.bazz_list.di

import com.waffiq.bazz_list.core.domain.usecase.note.NotesInteractor
import com.waffiq.bazz_list.core.domain.usecase.note.NotesUseCase
import com.waffiq.bazz_list.detailnotes.DetailNoteViewModel
import com.waffiq.bazz_list.listnotes.NotesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
  factory<NotesUseCase> { NotesInteractor(get()) }
}

val viewModelModule = module {
  viewModel { DetailNoteViewModel(get()) }
  viewModel { NotesViewModel(get()) }
}