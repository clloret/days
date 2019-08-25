package com.clloret.days.device.reminders;

import android.app.Notification;
import java.util.Date;

public interface ReminderUtils {

  void addReminder(Notification notification, String id, Date date);

  void removeReminder(String id);

  boolean isActive(String id);
}
