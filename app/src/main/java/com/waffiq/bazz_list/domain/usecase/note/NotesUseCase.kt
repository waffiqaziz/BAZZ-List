package com.waffiq.bazz_list.domain.usecase.note

import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.utils.helper.DbResult
import kotlinx.coroutines.flow.Flow

interface NotesUseCase {
  val notes: Flow<List<Note>>
  val notesSortDateDesc: Flow<List<Note>>
  suspend fun deleteNote(id: Int): DbResult
  suspend fun deleteMultipleNotes(notes: List<Note>): DbResult
  suspend fun deleteALlNotes(): DbResult
  suspend fun insertNote(note: Note): DbResult
  suspend fun updateNote(id: Int, title: String, description: String): DbResult
}