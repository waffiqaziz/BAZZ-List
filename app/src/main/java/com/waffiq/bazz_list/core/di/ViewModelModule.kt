package com.waffiq.bazz_list.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_list.core.ui.ViewModelFactory
import com.waffiq.bazz_list.detailnotes.DetailNoteViewModel
import com.waffiq.bazz_list.listnotes.NotesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
  @Binds
  @IntoMap
  @ViewModelKey(DetailNoteViewModel::class)
  abstract fun bindDetailNoteViewModel(viewModel: DetailNoteViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(NotesViewModel::class)
  abstract fun bindNotesViewModel(viewModel: NotesViewModel): ViewModel

  @Binds
  abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}