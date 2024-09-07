package com.waffiq.bazz_list.core.utils.helper

sealed class DbResult {
  data class Success(val message: String? = null) : DbResult()
  data class Error(val errorMessage: String) : DbResult()
}
