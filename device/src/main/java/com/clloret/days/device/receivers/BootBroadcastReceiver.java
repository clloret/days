package com.clloret.days.device.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.clloret.days.domain.reminders.EventRemindersManager;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import timber.log.Timber;

public class BootBroadcastReceiver extends BroadcastReceiver {

  @Inject
  EventRemindersManager eventRemindersManager;

  @Override
  public void onReceive(Context context, Intent intent) {

    AndroidInjection.inject(this, context);

    String action = intent.getAction();
    if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {

      Timber.d("ACTION_BOOT_COMPLETED");

      if (eventRemindersManager == null) {
        Timber.w("eventRemindersManager is null");

        return;
      }

      eventRemindersManager.scheduleReminderAll();
    }
  }
}
