package com.waffiq.bazz_list.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_list.core.domain.usecase.note.NotesUseCase
import com.waffiq.bazz_list.detailnotes.DetailNoteViewModel
import com.waffiq.bazz_list.di.AppScope
import com.waffiq.bazz_list.listnotes.NotesViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(
  private val notesUseCase: NotesUseCase,
) : ViewModelProvider.NewInstanceFactory() {


  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
      modelClass.isAssignableFrom(NotesViewModel::class.java) -> {
        NotesViewModel(notesUseCase) as T
      }
      modelClass.isAssignableFrom(DetailNoteViewModel::class.java) -> {
        DetailNoteViewModel(notesUseCase) as T
      }

      else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
  }
}