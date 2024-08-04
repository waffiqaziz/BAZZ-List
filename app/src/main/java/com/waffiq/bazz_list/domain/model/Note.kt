package com.waffiq.bazz_list.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
  val id: Int,
  val title: String,
  val description: String,
  val dateModified: Long,
  val hide: Boolean,
): Parcelable