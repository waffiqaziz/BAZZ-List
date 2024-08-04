package com.waffiq.bazz_list.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waffiq.bazz_list.data.local.model.NoteEntity
import com.waffiq.bazz_list.utils.common.Constants.NOTES_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
  @Query("SELECT * FROM $NOTES_TABLE_NAME")
  fun getAllNotes(): Flow<List<NoteEntity>>

  @Query("SELECT * FROM $NOTES_TABLE_NAME ORDER BY dateModified DESC")
  fun getAllNotesSortDateDesc(): Flow<List<NoteEntity>>

  @Query("DELETE FROM $NOTES_TABLE_NAME WHERE id = :id")
  suspend fun deleteItem(id: Int): Int

  @Query("DELETE FROM $NOTES_TABLE_NAME")
  suspend fun deleteALl(): Int

  @Query("DELETE FROM $NOTES_TABLE_NAME WHERE id IN (:id)")
  suspend fun deleteMultiple(id: List<Int>): Int

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(noteDao: NoteEntity)

  @Query("UPDATE $NOTES_TABLE_NAME SET title = :title, description = :description WHERE id = :id")
  suspend fun update(id: Int, title: String, description: String): Int
}