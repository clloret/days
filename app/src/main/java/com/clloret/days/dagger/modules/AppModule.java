package com.clloret.days.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.preference.PreferenceManager;
import com.clloret.days.activities.MainActivity;
import com.clloret.days.device.reminders.EventRemindersManagerImpl;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.reminders.EventRemindersManager;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

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
  static EventRemindersManager providesEventReminders(Application application,
      SharedPreferences preferences, EventBus eventBus, AppDataStore appDataStore) {

    return new EventRemindersManagerImpl(application, MainActivity.class, preferences, appDataStore
    );
  }

}