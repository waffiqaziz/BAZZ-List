package com.waffiq.bazz_list.ui.notes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.domain.usecase.note.NotesUseCase
import com.waffiq.bazz_list.utils.common.Event
import com.waffiq.bazz_list.utils.helper.DbResult
import kotlinx.coroutines.launch

class DetailNoteViewModel(
  private val notesUseCase: NotesUseCase
): ViewModel() {

  private val _dbResult = MutableLiveData<Event<DbResult>>()
  val dbResult: LiveData<Event<DbResult>> get() = _dbResult

  fun insertNote(note: Note) {
    viewModelScope.launch {
      val result = notesUseCase.insertNote(note)
      _dbResult.postValue(Event(result))
    }
  }

  fun updateNote(note: Note){
    viewModelScope.launch {
      val result = notesUseCase.updateNote(note.id, note.title, note.description)
      _dbResult.postValue(Event(result))
    }
  }
}