package com.waffiq.bazz_list.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel : ViewModel() {

  private val _text = MutableLiveData<String>().apply {
    value = "This is task Fragment"
  }
  val text: LiveData<String> = _text
}