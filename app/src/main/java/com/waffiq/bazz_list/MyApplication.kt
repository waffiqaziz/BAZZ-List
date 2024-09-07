package com.waffiq.bazz_list

import android.app.Application
import com.waffiq.bazz_list.core.di.databaseModule
import com.waffiq.bazz_list.core.di.repositoryModule
import com.waffiq.bazz_list.di.useCaseModule
import com.waffiq.bazz_list.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger(Level.NONE)
      androidContext(this@MyApplication)
      modules(
        listOf(
          databaseModule,
          repositoryModule,
          useCaseModule,
          viewModelModule
        )
      )
    }
  }
}