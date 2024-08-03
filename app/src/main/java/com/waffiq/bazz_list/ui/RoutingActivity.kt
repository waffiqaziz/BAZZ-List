package com.waffiq.bazz_list.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.waffiq.bazz_list.R

class RoutingActivity : AppCompatActivity() {
  private lateinit var splashScreen: SplashScreen

  override fun onCreate(savedInstanceState: Bundle?) {
    splashScreen = installSplashScreen()
    super.onCreate(savedInstanceState)
    splashScreen.setKeepOnScreenCondition { true }

    // change color navigation bar
    window.navigationBarColor = ContextCompat.getColor(this, R.color.gray_900)

    gotoMainActivity()
  }

  private fun gotoMainActivity() {
    startActivity(Intent(this, MainActivity::class.java))
    finish()
  }
}