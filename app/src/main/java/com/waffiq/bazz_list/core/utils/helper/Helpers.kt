package com.waffiq.bazz_list.core.utils.helper

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Helpers {
  fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("EEEE, MMMM dd yyyy  |  HH:mm", Locale.getDefault())
    return sdf.format(date)
  }

  fun formatTimestampCard(timestamp: Long): String {
    val currentDate = Calendar.getInstance()
    val date = Date(timestamp)
    val timestampCalendar = Calendar.getInstance().apply { time = date }

    val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    val sdfDay = SimpleDateFormat("MMM dd", Locale.getDefault())
    val sdfYear = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    return when {
      isSameDay(currentDate, timestampCalendar) -> {
        sdfTime.format(date)
      }

      isYesterday(timestampCalendar) -> {
        "Yesterday ${sdfTime.format(date)}"
      }

      isSameMonthAndYear(currentDate, timestampCalendar) -> {
        sdfDay.format(date)
      }

      isSameYear(currentDate, timestampCalendar) -> {
        sdfDay.format(date)
      }

      else -> {
        sdfYear.format(date)
      }
    }
  }

  private fun isSameDay(currentDate: Calendar, timestampCalendar: Calendar): Boolean {
    return currentDate.get(Calendar.YEAR) == timestampCalendar.get(Calendar.YEAR) &&
      currentDate.get(Calendar.DAY_OF_YEAR) == timestampCalendar.get(Calendar.DAY_OF_YEAR)
  }

  private fun isYesterday(timestampCalendar: Calendar): Boolean {
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_YEAR, -1)
    return yesterday.get(Calendar.YEAR) == timestampCalendar.get(Calendar.YEAR) &&
      yesterday.get(Calendar.DAY_OF_YEAR) == timestampCalendar.get(Calendar.DAY_OF_YEAR)
  }

  private fun isSameMonthAndYear(currentDate: Calendar, timestampCalendar: Calendar): Boolean {
    return currentDate.get(Calendar.YEAR) == timestampCalendar.get(Calendar.YEAR) &&
      currentDate.get(Calendar.MONTH) == timestampCalendar.get(Calendar.MONTH)
  }

  private fun isSameYear(currentDate: Calendar, timestampCalendar: Calendar): Boolean {
    return currentDate.get(Calendar.YEAR) == timestampCalendar.get(Calendar.YEAR)
  }

  fun dateNow(): String {
    return SimpleDateFormat("EEEE, MMMM dd yyyy  |  HH:mm ", Locale.getDefault()).format(Date())
  }

  fun Activity.setupStatusBar() {
    // change icon color status bar based on light/dark mode
    when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
      Configuration.UI_MODE_NIGHT_YES -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          window.insetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
          )
        } else {
          @Suppress("DEPRECATION")
          window.decorView.systemUiVisibility = 0
        }
      }

      Configuration.UI_MODE_NIGHT_NO -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          window.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
          )
        } else {
          @Suppress("DEPRECATION")
          window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
      }

      Configuration.UI_MODE_NIGHT_UNDEFINED -> {
      }

    }
  }
}