package com.clloret.days.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.preference.PreferenceManager;
import com.clloret.days.device.notifications.NotificationsIntents;
import com.clloret.days.device.reminders.ReminderManagerImpl;
import com.clloret.days.domain.reminders.ReminderManager;
import com.clloret.days.utils.NotificationsIntentsImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public abstract class AppModule {

  @Provides
  static Resources providesResources(Application application) {

    return application.getResources();
  }

  @Provides
  @Singleton
  static SharedPreferences providesPreferences(Application application) {

    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Binds
  abstract Context bindContext(Application application);

  @Binds
  abstract ReminderManager bindReminderManager(ReminderManagerImpl impl);

  @Binds
  abstract NotificationsIntents bindNotificationsIntents(NotificationsIntentsImpl impl);

}