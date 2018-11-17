package com.clloret.days.device.receivers;

import static com.clloret.days.device.reminders.RemindersUtils.ACTION_REMINDER;
import static com.clloret.days.device.reminders.RemindersUtils.NOTIFICATION;
import static com.clloret.days.device.reminders.RemindersUtils.NOTIFICATION_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Objects;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(final Context context, Intent intent) {

    if (Objects.equals(ACTION_REMINDER, intent.getAction())) {
      NotificationManager notificationManager = Objects.requireNonNull((NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE));

      Notification notification = intent.getParcelableExtra(NOTIFICATION);
      String notificationId = intent.getStringExtra(NOTIFICATION_ID);
      notificationManager.notify(notificationId, 0, notification);

      PendingIntent.getBroadcast(context, 0, intent,
          PendingIntent.FLAG_UPDATE_CURRENT).cancel();
    }
  }
}