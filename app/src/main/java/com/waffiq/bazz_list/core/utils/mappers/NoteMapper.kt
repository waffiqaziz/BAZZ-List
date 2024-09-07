package com.waffiq.bazz_list.core.utils.mappers

import com.waffiq.bazz_list.core.data.local.model.NoteEntity
import com.waffiq.bazz_list.core.domain.model.Note

object NoteMapper {
  fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    description = description,
    dateModified = dateModified,
    hide = hide,
  )

  fun Note.toNoteEntity() = NoteEntity(
    id = id,
    title = title,
    description = description,
    dateModified = dateModified,
    hide = hide,
  )
}