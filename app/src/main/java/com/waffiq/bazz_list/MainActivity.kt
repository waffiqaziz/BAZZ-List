package com.waffiq.bazz_list

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.waffiq.bazz_list.core.utils.helper.FabController
import com.waffiq.bazz_list.core.utils.helper.Helpers.setupStatusBar
import com.waffiq.bazz_list.core.utils.helper.OnFabClickListener
import com.waffiq.bazz_list.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FabController {

  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // setup action bar
    setSupportActionBar(binding.appBarMain.toolbar)

    // setup navigation drawer
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

  // implemented fab
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

  override fun showFab() {
    binding.appBarMain.fab.show()
  }

  override fun hideFab() {
    binding.appBarMain.fab.hide()
  }
}