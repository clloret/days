package com.clloret.days.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.preference.PreferenceManager;
import com.clloret.days.activities.MainActivity;
import com.clloret.days.device.reminders.ReminderManagerImpl;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.reminders.ReminderManager;
import com.clloret.days.domain.timelapse.TimeLapseManager;
import com.clloret.days.domain.utils.TimeProvider;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public abstract class AppModule {

  @Binds
  abstract Context provideContext(Application application);

  @Provides
  @Singleton
  static SharedPreferences providesPreferences(Application application) {

    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Provides
  @Singleton
  static Resources providesResources(Application application) {

    return application.getResources();
  }

  @Provides
  @Singleton
  static ReminderManager providesReminderManager(Application application,
      SharedPreferences preferences) {

    return new ReminderManagerImpl(application, MainActivity.class, preferences
    );
  }

  @Provides
  @Singleton
  static EventReminderManager providesEventReminders(ReminderManager reminderManager,
      TimeProvider timeProvider) {

    return new EventReminderManager(reminderManager, timeProvider);
  }

  @Provides
  @Singleton
  static TimeLapseManager providesTimeLapseManager(TimeProvider timeProvider,
      EventReminderManager eventReminderManager, AppDataStore appDataStore) {

    return new TimeLapseManager(timeProvider, eventReminderManager, appDataStore);
  }

}