package com.waffiq.bazz_list.data.local.datasource

import android.util.Log
import com.waffiq.bazz_list.data.local.model.NoteEntity
import com.waffiq.bazz_list.data.local.room.NoteDao
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.utils.helper.DbResult
import kotlinx.coroutines.flow.Flow

class LocalDataSource private constructor(private val noteDao: NoteDao) : LocalDataSourceInterface {
  override fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()
  override fun getAllNotesSortDateDesc() = noteDao.getAllNotesSortDateDesc()

  override suspend fun deleteNote(id: Int): DbResult =
    executeDbOperation { noteDao.deleteItem(id) }

  override suspend fun deleteMultipleNotes(notes: List<Note>): DbResult =
    executeDbOperation { noteDao.deleteMultiple(notes.map { it.id }) }

  override suspend fun deleteALlNotes(): DbResult =
    executeDbOperation { noteDao.deleteALl() }

  override suspend fun insertNote(noteEntity: NoteEntity): DbResult =
    executeDbOperation { noteDao.insert(noteEntity) }

  override suspend fun updateNote(
    id: Int,
    title: String,
    description: String
  ): DbResult = executeDbOperation {
    noteDao.update(id, title, description)
  }

  companion object {
    private var instance: LocalDataSource? = null

    fun getInstance(noteDao: NoteDao): LocalDataSource =
      instance ?: synchronized(this) { instance ?: LocalDataSource(noteDao) }
  }
}