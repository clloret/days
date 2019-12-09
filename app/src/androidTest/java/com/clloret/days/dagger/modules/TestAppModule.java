package com.clloret.days.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.preference.PreferenceManager;
import com.clloret.days.device.notifications.NotificationsIntents;
import com.clloret.days.device.reminders.ReminderManagerImpl;
import com.clloret.days.domain.reminders.ReminderManager;
import com.clloret.days.screenshots.locale.LocaleContextWrapper;
import com.clloret.days.utils.NotificationsIntentsImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.Locale;
import javax.inject.Singleton;

@Module
public abstract class TestAppModule {

  @Provides
  static Context providesContext(Application application, Locale locale) {

    return LocaleContextWrapper.wrap(application, locale);
  }

  @Provides
  static Resources providesResources(Application application, Locale locale) {

    return LocaleContextWrapper.wrap(application, locale).getResources();
  }

  @Provides
  @Singleton
  static SharedPreferences providesPreferences(Application application) {

    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Binds
  abstract ReminderManager bindReminderManager(ReminderManagerImpl impl);

  @Binds
  abstract NotificationsIntents bindNotificationsIntents(NotificationsIntentsImpl impl);

}
