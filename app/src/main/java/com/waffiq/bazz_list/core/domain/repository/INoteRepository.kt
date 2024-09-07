package com.waffiq.bazz_list.core.domain.repository

import com.waffiq.bazz_list.core.domain.model.Note
import com.waffiq.bazz_list.core.utils.helper.DbResult
import kotlinx.coroutines.flow.Flow

interface INoteRepository {
  val getAllNotes: Flow<List<Note>>
  val getAllNotesSortDateDesc: Flow<List<Note>>
  suspend fun insert(note: Note): DbResult
  suspend fun update(id: Int, title: String, description: String, dateModified: Long): DbResult
  suspend fun deleteItem(id: Int): DbResult
  suspend fun deleteMultiple(notes: List<Note>): DbResult
  suspend fun deleteALl(): DbResult
}