package com.clloret.days.device.reminders;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.clloret.days.device.notifications.NotificationsFactory;
import com.clloret.days.device.receivers.AlarmBroadcastReceiver;
import java.util.Date;

public class RemindersUtils {

  public static final String ACTION_REMINDER = "com.clloret.days.REMINDER";
  public static final String NOTIFICATION_ID = "notification_id";
  public static final String NOTIFICATION = "notification";

  private final Context context;
  private final NotificationsFactory notificationsFactory;
  private final PendingIntent notificationPendingIntent;

  RemindersUtils(NotificationsFactory notificationsFactory, Context context, Class<?> cls) {

    this.context = context;
    this.notificationsFactory = notificationsFactory;

    Intent intent = new Intent(context, cls);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    this.notificationPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
  }

  void addReminder(String id, String message, Date date) {

    Notification notification = notificationsFactory.createNotification(notificationPendingIntent,
        message);

    Intent intent = getNotificationIntent(id);
    intent.putExtra(NOTIFICATION_ID, id);
    intent.putExtra(NOTIFICATION, notification);

    AlarmUtils.addAlarm(context, intent, date.getTime());
  }

  void removeReminder(String id) {

    Intent intent = getNotificationIntent(id);

    AlarmUtils.cancelAlarm(context, intent);
  }

  private Intent getNotificationIntent(String id) {

    Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
    intent.setData(Uri.parse("custom://" + id));
    intent.setAction(ACTION_REMINDER);
    return intent;
  }

  public boolean isActive(String id) {

    Intent notificationIntent = getNotificationIntent(id);

    return AlarmUtils.hasAlarm(context, notificationIntent);
  }

}