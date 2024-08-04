package com.waffiq.bazz_list.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.waffiq.bazz_list.R
import com.waffiq.bazz_list.databinding.ActivityMainBinding
import com.waffiq.bazz_list.utils.helper.OnFabClickListener

class MainActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setSupportActionBar(binding.appBarMain.toolbar)

    val drawerLayout: DrawerLayout = binding.drawerLayout
    val navView: NavigationView = binding.navView

    appBarConfiguration = AppBarConfiguration(
      setOf(
        R.id.nav_note, R.id.nav_task, R.id.nav_slideshow
      ), drawerLayout
    )

    val navController =
      supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment_content_main)?.findNavController()

    if (navController != null) {
      setupActionBarWithNavController(navController, appBarConfiguration)
      navView.setupWithNavController(navController)
    }

    setupStatusBar()
    fabClickListener()
  }

  private fun setupStatusBar() {
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

  private fun fabClickListener() {

    binding.appBarMain.fab.setOnClickListener { view ->

      val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
      val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
      if (currentFragment is OnFabClickListener) currentFragment.onFabClick()
    }
  }


  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment_content_main)
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}