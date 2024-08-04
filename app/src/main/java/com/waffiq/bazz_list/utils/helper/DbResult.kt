package com.waffiq.bazz_list.utils.helper

sealed class DbResult {
  data class Success(val message: String? = null) : DbResult()
  data class Error(val errorMessage: String) : DbResult()
}
