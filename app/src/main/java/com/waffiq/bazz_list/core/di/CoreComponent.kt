package com.waffiq.bazz_list.core.di

import android.content.Context
import com.waffiq.bazz_list.core.domain.repository.INoteRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
  modules = [RepositoryModule::class]
)
interface CoreComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance context: Context): CoreComponent
  }

  fun provideRepository() : INoteRepository
}