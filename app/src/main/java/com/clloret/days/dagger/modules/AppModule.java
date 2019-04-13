package com.clloret.days.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.preference.PreferenceManager;
import com.clloret.days.device.notifications.NotificationsIntents;
import com.clloret.days.device.reminders.ReminderManagerImpl;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.reminders.ReminderManager;
import com.clloret.days.domain.timelapse.TimeLapseManager;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.utils.NotificationsIntentsImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AppModule {

  @Provides
  Context providesContext(Application application) {

    return application;
  }

  @Provides
  Resources providesResources(Application application) {

    return application.getResources();
  }

  @Provides
  @Singleton
  SharedPreferences providesPreferences(Application application) {

    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Provides
  @Singleton
  ReminderManager providesReminderManager(Application application,
      NotificationsIntents notificationsIntents, StringResourceProvider stringResourceProvider) {

    return new ReminderManagerImpl(application, notificationsIntents, stringResourceProvider);
  }

  @Provides
  @Singleton
  EventReminderManager providesEventReminders(ReminderManager reminderManager,
      TimeProvider timeProvider, PreferenceUtils preferenceUtils) {

    return new EventReminderManager(reminderManager, timeProvider, preferenceUtils);
  }

  @Provides
  @Singleton
  TimeLapseManager providesTimeLapseManager(TimeProvider timeProvider,
      EventReminderManager eventReminderManager, AppDataStore appDataStore) {

    return new TimeLapseManager(timeProvider, eventReminderManager, appDataStore);
  }

  @Provides
  @Singleton
  NotificationsIntents providesNotificationsIntents(Context context,
      EventViewModelMapper eventViewModelMapper) {

    return new NotificationsIntentsImpl(context, eventViewModelMapper);
  }

}