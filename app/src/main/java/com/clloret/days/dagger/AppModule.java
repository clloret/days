package com.clloret.days.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
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

}