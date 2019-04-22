package com.clloret.days.device.receivers;

import static com.clloret.days.device.reminders.ReminderUtilsImpl.ACTION_REMINDER;
import static com.clloret.days.device.reminders.ReminderUtilsImpl.EXTRA_NOTIFICATION;
import static com.clloret.days.device.reminders.ReminderUtilsImpl.EXTRA_NOTIFICATION_ID;

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

      Notification notification = intent.getParcelableExtra(EXTRA_NOTIFICATION);
      String notificationId = intent.getStringExtra(EXTRA_NOTIFICATION_ID);
      int hashCode = notificationId.hashCode();
      notificationManager.notify(notificationId, hashCode, notification);

      PendingIntent.getBroadcast(context, hashCode, intent,
          PendingIntent.FLAG_NO_CREATE).cancel();
    }
  }
}