package com.waffiq.bazz_list.di

import com.waffiq.bazz_list.core.di.CoreComponent
import com.waffiq.bazz_list.detailnotes.DetailNoteActivity
import com.waffiq.bazz_list.listnotes.NotesFragment
import dagger.Component

@AppScope
@Component(
  dependencies = [CoreComponent::class],
  modules = [AppModule::class]
)
interface AppComponent {
  @Component.Factory
  interface Factory {
    fun create(coreComponent: CoreComponent): AppComponent
  }

  fun inject(fragment: NotesFragment)
  fun inject(activity: DetailNoteActivity)
}