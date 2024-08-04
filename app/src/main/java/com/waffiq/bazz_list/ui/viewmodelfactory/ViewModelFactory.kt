package com.waffiq.bazz_list.ui.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.waffiq.bazz_list.di.module.Injection
import com.waffiq.bazz_list.domain.usecase.note.NotesUseCase
import com.waffiq.bazz_list.ui.notes.DetailNoteViewModel
import com.waffiq.bazz_list.ui.notes.NotesViewModel

class ViewModelFactory(
  private val notesUseCase: NotesUseCase,
) : ViewModelProvider.NewInstanceFactory() {

  companion object {
    @Volatile
    private var instance: ViewModelFactory? = null

    fun getInstance(context: Context): ViewModelFactory =
      instance ?: synchronized(this) {
        instance ?: ViewModelFactory(
          Injection.provideGetAllNoteUseCase(context),
        )
      }
  }

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