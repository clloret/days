package com.clloret.days;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import com.clloret.days.dagger.AppComponent;
import com.clloret.days.dagger.DaggerAppComponent;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.device.services.TimeLapseJob;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.utils.ThreadSchedulers;
import com.clloret.days.utils.StethoUtils;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class App extends DaggerApplication
    implements HasSupportFragmentInjector, HasBroadcastReceiverInjector, HasServiceInjector {

  private static final String ROBOLECTRIC_FINGERPRINT = "robolectric";

  @Inject
  DispatchingAndroidInjector<Fragment> supportFragmentDispatchingInjector;

  @Inject
  DispatchingAndroidInjector<BroadcastReceiver> broadcastReceiverDispatchingInjector;

  @Inject
  DispatchingAndroidInjector<Service> serviceDispatchingInjector;

  @Inject
  CacheSource<Event> eventCacheSource;

  @Inject
  CacheSource<Tag> tagCacheSource;

  @Inject
  ThreadSchedulers threadSchedulers;

  public static App get(Context context) {

    return (App) context.getApplicationContext();
  }

  private static boolean isRoboUnitTest() {

    return ROBOLECTRIC_FINGERPRINT.equals(Build.FINGERPRINT);
  }

  @Override
  public void onCreate() {

    super.onCreate();

    Timber.plant(new DebugTree());
    if (!isRoboUnitTest()) {
      StethoUtils.install(this);
    }

    TimeLapseJob.scheduleJob(this);
  }

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

    AppComponent appComponent = DaggerAppComponent.builder().application(this).build();
    appComponent.inject(this);

    return appComponent;
  }

  @Override
  public DispatchingAndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {

    return broadcastReceiverDispatchingInjector;
  }

  @Override
  public DispatchingAndroidInjector<Service> serviceInjector() {

    return serviceDispatchingInjector;
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {

    return supportFragmentDispatchingInjector;
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
