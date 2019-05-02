package com.clloret.days.device.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import dagger.android.AndroidInjection;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import timber.log.Timber;

public class BootBroadcastReceiver extends BroadcastReceiver {

  @Inject
  EventReminderManager eventReminderManager;

  @Inject
  EventRepository appDataStore;

  @Override
  public void onReceive(Context context, Intent intent) {

    AndroidInjection.inject(this, context);

    String action = intent.getAction();
    if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {

      Timber.d("ACTION_BOOT_COMPLETED");

      if (eventReminderManager == null) {
        Timber.w("eventReminderManager is null");

        return;
      }

      scheduleAllReminders();
    }
  }

  private void scheduleAllReminders() {

    appDataStore.getAll(true)
        .doOnSuccess(events -> eventReminderManager.scheduleReminders(events, false))
        .subscribeOn(Schedulers.io())
        .subscribe();
  }
}
