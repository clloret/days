package com.clloret.days.dagger

import android.app.Application
import com.clloret.days.App
import com.clloret.days.dagger.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
          ActivityModule::class,
          AndroidInjectionModule::class,
          AppModule::class,
          BroadcastReceiverModule::class,
          DataModule::class,
          EventSortModule::class,
          ServiceModule::class,
          ThreadingModule::class,
          UtilsModule::class
        ]
)
interface AppComponent : AndroidInjector<App> {

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }
}
