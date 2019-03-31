package com.clloret.days.device.notifications;

import static android.os.Build.VERSION.SDK_INT;
import static android.support.v4.app.NotificationCompat.CATEGORY_REMINDER;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.Builder;
import com.clloret.days.device.R;

public class NotificationsFactory {

  private static final long[] VIBRATION_PATTERN = {100, 200, 300, 400, 500, 400, 300, 200, 400};
  private static final String CHANNEL_REMINDERS_ID = "channel_reminders_id";

  private final Context context;
  private final Resources resources;
  private final NotificationsUtils notificationsUtils;

  public NotificationsFactory(Context context, Resources resources,
      NotificationsUtils notificationsUtils) {

    this.context = context;
    this.resources = resources;
    this.notificationsUtils = notificationsUtils;
  }

  public Notification createEventReminderNotification(PendingIntent contentIntent, String message,
      Iterable<Action> actions) {

    Builder builder = getBuilderFromChannel();

    for (Action action : actions) {
      builder.addAction(action);
    }

    builder.setContentTitle(message)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentText(resources.getString(R.string.app_name))
        .setAutoCancel(true)
        .setContentIntent(contentIntent)
        .setTicker(message)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setChannelId(CHANNEL_REMINDERS_ID)
        .setCategory(CATEGORY_REMINDER)
        .setVibrate(VIBRATION_PATTERN);

    return builder.build();
  }

  @NonNull
  private Builder getBuilderFromChannel() {

    Builder builder;

    if (SDK_INT >= VERSION_CODES.O) {
      NotificationChannel channel = notificationsUtils.getNotificationChannel(CHANNEL_REMINDERS_ID);
      if (channel == null) {
        String channelName = resources.getString(R.string.channel_reminders_name);
        notificationsUtils
            .createNotificationChannel(CHANNEL_REMINDERS_ID, channelName, VIBRATION_PATTERN);
      }
      builder = new Builder(context, CHANNEL_REMINDERS_ID);
    } else {

      builder = new Builder(context, CHANNEL_REMINDERS_ID);
      builder.setPriority(Notification.PRIORITY_HIGH);
    }

    return builder;
  }

}
