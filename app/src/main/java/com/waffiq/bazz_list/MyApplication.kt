package com.waffiq.bazz_list

import android.app.Application
import com.waffiq.bazz_list.core.di.CoreComponent
import com.waffiq.bazz_list.core.di.DaggerCoreComponent
import com.waffiq.bazz_list.di.AppComponent
import com.waffiq.bazz_list.di.DaggerAppComponent

open class MyApplication : Application() {

  private val coreComponent: CoreComponent by lazy {
    DaggerCoreComponent.factory().create(applicationContext)
  }

  val appComponent: AppComponent by lazy {
    DaggerAppComponent.factory().create(coreComponent)
  }
}