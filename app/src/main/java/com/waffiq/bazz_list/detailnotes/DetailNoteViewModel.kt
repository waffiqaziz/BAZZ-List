package com.waffiq.bazz_list.detailnotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_list.core.domain.model.Note
import com.waffiq.bazz_list.core.domain.usecase.note.NotesUseCase
import com.waffiq.bazz_list.core.utils.common.Event
import com.waffiq.bazz_list.core.utils.helper.DbResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailNoteViewModel @Inject constructor(
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
      val result = notesUseCase.updateNote(note.id, note.title, note.description, note.dateModified)
      _dbResult.postValue(Event(result))
    }
  }
}