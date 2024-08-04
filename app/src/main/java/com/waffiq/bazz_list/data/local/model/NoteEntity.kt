package com.waffiq.bazz_list.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waffiq.bazz_list.utils.common.Constants.NOTES_TABLE_NAME

@Entity(tableName = NOTES_TABLE_NAME)
data class NoteEntity (
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  val id: Int = 0,

  @ColumnInfo(name = "title")
  val title: String,

  @ColumnInfo(name = "description")
  val description: String,

  @ColumnInfo(name = "dateModified")
  val dateModified: Long,

//  @ColumnInfo(name = "reminder")
//  val reminder: String,

//  @ColumnInfo(name = "color")
//  val color: String,

  @ColumnInfo(name = "hide")
  val hide: Boolean,

//  @ColumnInfo(name = "folder")
//  val folder: String,
)