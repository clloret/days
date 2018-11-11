package com.clloret.days.device.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Objects;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

  public static final String NOTIFICATION_ID = "notification_id";
  public static final String NOTIFICATION = "notification";

  @Override
  public void onReceive(final Context context, Intent intent) {

    NotificationManager notificationManager = Objects.requireNonNull((NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE));

    Notification notification = intent.getParcelableExtra(NOTIFICATION);
    String notificationId = intent.getStringExtra(NOTIFICATION_ID);
    notificationManager.notify(notificationId, 0, notification);

    PendingIntent.getBroadcast(context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT).cancel();
  }
}