package com.waffiq.bazz_list.listnotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.waffiq.bazz_list.core.domain.model.Note
import com.waffiq.bazz_list.core.domain.usecase.note.NotesUseCase
import com.waffiq.bazz_list.core.utils.common.Event
import com.waffiq.bazz_list.core.utils.helper.DbResult
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
  private val notesUseCase: NotesUseCase
) : ViewModel() {

  private val _dbResult = MutableLiveData<Event<DbResult>>()
  val dbResult: LiveData<Event<DbResult>> get() = _dbResult

  val notes = notesUseCase.notes.asLiveData().distinctUntilChanged()

  fun deleteNote(id: Int){
    viewModelScope.launch {
      val result = notesUseCase.deleteNote(id)
      _dbResult.postValue(Event(result))
    }
  }

  fun deleteMultipleNotes(notes: List<Note>){
    viewModelScope.launch {
      val result = notesUseCase.deleteMultipleNotes(notes)
      _dbResult.postValue(Event(result))
    }
  }
}