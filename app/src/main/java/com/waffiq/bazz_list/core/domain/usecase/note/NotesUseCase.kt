package com.waffiq.bazz_list.core.domain.usecase.note

import com.waffiq.bazz_list.core.domain.model.Note
import com.waffiq.bazz_list.core.utils.helper.DbResult
import kotlinx.coroutines.flow.Flow

interface NotesUseCase {
  val notes: Flow<List<Note>>
  val notesSortDateDesc: Flow<List<Note>>
  suspend fun deleteNote(id: Int): DbResult
  suspend fun deleteMultipleNotes(notes: List<Note>): DbResult
  suspend fun deleteALlNotes(): DbResult
  suspend fun insertNote(note: Note): DbResult
  suspend fun updateNote(id: Int, title: String, description: String, dateModified: Long): DbResult
}