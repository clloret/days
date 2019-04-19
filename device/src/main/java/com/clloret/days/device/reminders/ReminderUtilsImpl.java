package com.clloret.days.device.reminders;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.clloret.days.device.receivers.AlarmBroadcastReceiver;
import java.util.Date;
import timber.log.Timber;

public class ReminderUtilsImpl implements ReminderUtils {

  public static final String ACTION_REMINDER = "com.clloret.days.action.REMINDER";
  public static final String EXTRA_NOTIFICATION_ID = "com.clloret.days.extras.EXTRA_NOTIFICATION_ID";
  public static final String EXTRA_NOTIFICATION = "com.clloret.days.extras.EXTRA_NOTIFICATION";

  private final Context context;

  ReminderUtilsImpl(Context context) {

    this.context = context;
  }

  @Override
  public void addReminder(Notification notification, String id, String message, Date date) {

    Timber.d("addReminder - id: %s, message: %s, date: %s", id, message, date.toString());

    Intent intent = getNotificationIntent(id);
    intent.putExtra(EXTRA_NOTIFICATION_ID, id);
    intent.putExtra(EXTRA_NOTIFICATION, notification);

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