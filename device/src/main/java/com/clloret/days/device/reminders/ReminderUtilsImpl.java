package com.clloret.days.device.reminders;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.clloret.days.device.notifications.NotificationsFactory;
import com.clloret.days.device.receivers.AlarmBroadcastReceiver;
import com.clloret.days.domain.reminders.ReminderUtils;
import java.util.Date;
import timber.log.Timber;

public class ReminderUtilsImpl implements ReminderUtils {

  public static final String ACTION_REMINDER = "com.clloret.days.REMINDER";
  public static final String NOTIFICATION_ID = "notification_id";
  public static final String NOTIFICATION = "notification";

  private final Context context;
  private final NotificationsFactory notificationsFactory;
  private final PendingIntent notificationPendingIntent;

  ReminderUtilsImpl(NotificationsFactory notificationsFactory, Context context, Class<?> cls) {

    this.context = context;
    this.notificationsFactory = notificationsFactory;

    Intent intent = new Intent(context, cls);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    this.notificationPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
  }

  @Override
  public void addReminder(String id, String message, Date date) {

    Timber.d("addReminder - id: %s, message: %s, date: %s", id, message, date.toString());

    Notification notification = notificationsFactory.createNotification(notificationPendingIntent,
        message);

    Intent intent = getNotificationIntent(id);
    intent.putExtra(NOTIFICATION_ID, id);
    intent.putExtra(NOTIFICATION, notification);

    AlarmUtils.addAlarm(context, intent, date.getTime());
  }

  @Override
  public void removeReminder(String id) {

    Timber.d("removeReminder - id: %s", id);

    Intent intent = getNotificationIntent(id);

    AlarmUtils.cancelAlarm(context, intent);
  }

  private Intent getNotificationIntent(String id) {

    Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
    intent.setData(Uri.parse("custom://" + id));
    intent.setAction(ACTION_REMINDER);
    return intent;
  }

  @Override
  public boolean isActive(String id) {

    Intent notificationIntent = getNotificationIntent(id);

    return AlarmUtils.hasAlarm(context, notificationIntent);
  }

}