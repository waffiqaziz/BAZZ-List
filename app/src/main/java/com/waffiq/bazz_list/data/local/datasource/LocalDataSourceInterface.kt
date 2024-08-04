package com.waffiq.bazz_list.data.local.datasource

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDiskIOException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import android.util.Log
import com.waffiq.bazz_list.data.local.model.NoteEntity
import com.waffiq.bazz_list.domain.model.Note
import com.waffiq.bazz_list.utils.helper.DbResult
import kotlinx.coroutines.flow.Flow

interface LocalDataSourceInterface {

  fun getAllNotes(): Flow<List<NoteEntity>>
  fun getAllNotesSortDateDesc(): Flow<List<NoteEntity>>

  suspend fun insertNote(noteEntity: NoteEntity): DbResult
  suspend fun updateNote(id: Int, title: String, description: String): DbResult
  suspend fun deleteNote(id: Int): DbResult
  suspend fun deleteMultipleNotes(notes: List<Note>): DbResult
  suspend fun deleteALlNotes(): DbResult

  suspend fun <T> executeDbOperation(
    operation: suspend () -> T
  ): DbResult {
    return try {
      operation.invoke()
      DbResult.Success() // Operation successful
    } catch (e: SQLiteConstraintException) {
      Log.e("DatabaseError", "Operation failed due to unique constraint violation: ${e.message}")
      DbResult.Error("Unique constraint violation: ${e.message}")
    } catch (e: SQLiteFullException) {
      Log.e("DatabaseError", "Operation failed because the database is full: ${e.message}")
      DbResult.Error("Database is full: ${e.message}")
    } catch (e: SQLiteDiskIOException) {
      Log.e("DatabaseError", "Operation failed due to disk IO issue: ${e.message}")
      DbResult.Error("Disk IO issue: ${e.message}")
    } catch (e: SQLiteException) {
      Log.e("DatabaseError", "Operation failed due to SQLite exception: ${e.message}")
      DbResult.Error("SQLite exception: ${e.message}")
    } catch (e: Exception) {
      Log.e("DatabaseError", "Operation failed due to unknown error: ${e.message}")
      DbResult.Error("Unknown error: ${e.message}")
    }
  }

  // Define error codes
  companion object {
    const val SUCCESS = 0
    const val ERROR_DUPLICATE_ENTRY = 1
    const val ERROR_SQLITE_EXCEPTION = 2
    const val ERROR_DATABASE_FULL = 3
    const val ERROR_DISK_IO = 4
    const val ERROR_UNKNOWN = 5
  }
}