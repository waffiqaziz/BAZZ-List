package com.waffiq.bazz_list.core.domain.usecase.note

import com.waffiq.bazz_list.core.domain.repository.INoteRepository
import com.waffiq.bazz_list.core.domain.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesInteractor @Inject constructor(
  private val iNoteRepository: INoteRepository
) : NotesUseCase {
  override val notes: Flow<List<Note>> = iNoteRepository.getAllNotes
  override val notesSortDateDesc: Flow<List<Note>> = iNoteRepository.getAllNotesSortDateDesc
  override suspend fun deleteNote(id: Int) = iNoteRepository.deleteItem(id)
  override suspend fun deleteMultipleNotes(notes: List<Note>) =
    iNoteRepository.deleteMultiple(notes)

  override suspend fun deleteALlNotes() = iNoteRepository.deleteALl()

  override suspend fun insertNote(note: Note) =
    iNoteRepository.insert(note)

  override suspend fun updateNote(id: Int, title: String, description: String, dateModified: Long) =
    iNoteRepository.update(id, title, description, dateModified)
}