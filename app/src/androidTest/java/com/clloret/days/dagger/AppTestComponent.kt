package com.clloret.days.dagger

import android.app.Application
import com.clloret.days.NotificationsTest
import com.clloret.days.TestApp
import com.clloret.days.activities.MainActivityTest
import com.clloret.days.dagger.modules.*
import com.clloret.days.screenshots.BaseScreenshotsTest
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import java.util.*
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
          ActivityModule::class,
          AndroidInjectionModule::class,
          TestAppModule::class,
          BroadcastReceiverModule::class,
          TestDataModule::class,
          EventSortModule::class,
          ServiceModule::class,
          TestThreadingModule::class,
          TestUseCasesModule::class,
          TestUtilsModule::class
        ]
)
interface AppTestComponent : AndroidInjector<TestApp> {

  fun inject(test: NotificationsTest)

  fun inject(test: MainActivityTest)

  fun inject(test: BaseScreenshotsTest)

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(application: Application): Builder

    @BindsInstance
    fun locale(locale: Locale): Builder

    fun build(): AppTestComponent
  }
}
