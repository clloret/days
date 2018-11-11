package com.clloret.days.device.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.clloret.days.device.events.ReminderAllScheduleEvent;
import org.greenrobot.eventbus.EventBus;
import timber.log.Timber;

public class BootBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    String action = intent.getAction();
    if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
      Timber.d("ACTION_BOOT_COMPLETED");

      EventBus.getDefault().post(new ReminderAllScheduleEvent());
    }
  }
}
