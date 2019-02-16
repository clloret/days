package com.clloret.days.device.reminders;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import com.clloret.days.device.R;
import com.clloret.days.device.notifications.NotificationsFactory;
import com.clloret.days.device.notifications.NotificationsUtils;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.reminders.ReminderManager;
import com.clloret.days.domain.reminders.ReminderUtils;
import java.util.Date;
import java.util.Objects;

public class ReminderManagerImpl implements ReminderManager {

  private static final int DEFAULT_REMINDER_TIME = 0;
  private final ReminderUtils reminderUtils;
  private final SharedPreferences preferences;
  private final Resources resources;

  public ReminderManagerImpl(Context context, Class<?> cls, SharedPreferences preferences) {

    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationsUtils notificationsUtils = new NotificationsUtils(
        Objects.requireNonNull(notificationManager));
    NotificationsFactory notificationsFactory = new NotificationsFactory(context,
        context.getResources(),
        notificationsUtils);

    this.reminderUtils = new ReminderUtilsImpl(notificationsFactory, context, cls);
    this.preferences = preferences;
    this.resources = context.getResources();
  }

  @Override
  public void addReminder(String id, String message, Date date) {

    reminderUtils.addReminder(id, message, date);
  }

  @Override
  public void removeReminderForEvent(Event event) {

    reminderUtils.removeReminder(event.getId());
  }

  @Override
  public int getReminderTime() {

    return preferences
        .getInt(resources.getString(R.string.pref_reminder_time), DEFAULT_REMINDER_TIME);
  }

}
