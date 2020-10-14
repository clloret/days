package com.clloret.days;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.clloret.days.dagger.AppComponent;
import com.clloret.days.dagger.DaggerAppComponent;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.device.services.TimeLapseJob;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.ThreadSchedulers;
import com.clloret.days.utils.StethoUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.DaggerApplication;
import javax.inject.Inject;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;

public class App extends DaggerApplication
    implements HasAndroidInjector {

  private static final String ROBOLECTRIC_FINGERPRINT = "robolectric";

  @Inject
  DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

  @Inject
  CacheSource<Event> eventCacheSource;

  @Inject
  CacheSource<Tag> tagCacheSource;

  @Inject
  ThreadSchedulers threadSchedulers;

  @Inject
  PreferenceUtils preferenceUtils;

  @Override
  public void onCreate() {

    super.onCreate();

    configureLog();

    configureAnalytics();

    TimeLapseJob.scheduleJob(this);
  }

  @Override
  public AndroidInjector<Object> androidInjector() {

    return dispatchingAndroidInjector;
  }

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

    AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
    appComponent.inject(this);

    return appComponent;
  }

  public static App get(Context context) {

    return (App) context.getApplicationContext();
  }

  private static boolean isRoboUnitTest() {

    return ROBOLECTRIC_FINGERPRINT.equals(Build.FINGERPRINT);
  }

  private void configureLog() {

    Timber.plant(new DebugTree());
    if (!isRoboUnitTest()) {
      StethoUtils.install(this);
    }
  }

  private void configureAnalytics() {

    if (!BuildConfig.DEBUG && preferenceUtils.isAnalyticsEnabled()) {
      FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
      analytics.setAnalyticsCollectionEnabled(true);
      //Fabric.with(this, new Crashlytics());
    }
  }

  public void restart() {

    Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());

    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
        PendingIntent.getActivity(getBaseContext(), 0, new Intent(intent),
            intent.getFlags()));

    System.exit(0);
  }

  public void invalidateDataAndRestart() {

    eventCacheSource.deleteAll()
        .andThen(tagCacheSource.deleteAll())
        .doOnComplete(this::restart)
        .subscribeOn(threadSchedulers.getExecutorScheduler())
        .observeOn(threadSchedulers.getUiScheduler())
        .subscribe();
  }

}
