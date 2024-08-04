package com.waffiq.bazz_list.data.repository

import com.waffiq.bazz_list.data.local.datasource.LocalDataSource
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.domain.repository.INoteRepository
import com.waffiq.bazz_list.utils.helper.DbResult
import com.waffiq.bazz_list.utils.mappers.NoteMapper.toNote
import com.waffiq.bazz_list.utils.mappers.NoteMapper.toNoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepository(
  private val localDataSource: LocalDataSource
) : INoteRepository {
  override val getAllNotes: Flow<List<Note>> =
    localDataSource.getAllNotes().map { list ->
      list.map { it.toNote() }
    }
  override val getAllNotesSortDateDesc: Flow<List<Note>> =
    localDataSource.getAllNotesSortDateDesc().map { list ->
      list.map { it.toNote() }
    }

  override suspend fun deleteItem(id: Int): DbResult =
    localDataSource.deleteNote(id)

  override suspend fun deleteMultiple(notes: List<Note>): DbResult =
    localDataSource.deleteMultipleNotes(notes)

  override suspend fun deleteALl(): DbResult =
    localDataSource.deleteALlNotes()

  override suspend fun insert(note: Note): DbResult =
    localDataSource.insertNote(note.toNoteEntity())

  override suspend fun update(id: Int, title: String, description: String, dateModified: Long): DbResult =
    localDataSource.updateNote(id, title, description, dateModified)


}