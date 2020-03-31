package com.clloret.days.dagger.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.clloret.days.device.notifications.NotificationsIntents
import com.clloret.days.device.reminders.ReminderManagerImpl
import com.clloret.days.domain.reminders.ReminderManager
import com.clloret.days.utils.NotificationsIntentsImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule(private val app: Application) {

  @Binds
  abstract fun bindContext(app: Application): Context

  @Binds
  abstract fun bindReminderManager(impl: ReminderManagerImpl): ReminderManager

  @Binds
  abstract fun bindNotificationsIntents(impl: NotificationsIntentsImpl): NotificationsIntents

  @Module
  companion object {
    @Provides
    fun providePreferences(app: Application): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    fun provideResources(app: Application): Resources = app.resources
  }

}
