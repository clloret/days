package com.clloret.days.device.notifications;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import timber.log.Timber;

public class NotificationsUtils {

  private final NotificationManager notificationManager;

  public NotificationsUtils(@NonNull NotificationManager notificationManager) {

    this.notificationManager = notificationManager;
  }

  public void showNotification(int notificationId, Notification notification) {

    notificationManager.notify(notificationId, notification);
  }

  public void cancelNotification(String notificationId) {

    Timber.d("cancelNotification: %s", notificationId);

    notificationManager.cancel(notificationId, notificationId.hashCode());
  }

  void createNotificationChannel(String channelId, String channelName, long[] vibrationPattern) {

    if (SDK_INT >= VERSION_CODES.O) {
      NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
      if (channel == null) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        channel = new NotificationChannel(channelId, channelName, importance);
        channel.enableVibration(true);
        channel.setVibrationPattern(vibrationPattern);
        notificationManager.createNotificationChannel(channel);
      }
    }
  }

  @RequiresApi(api = VERSION_CODES.O)
  NotificationChannel getNotificationChannel(String channelId) {

    return notificationManager.getNotificationChannel(channelId);
  }
}
