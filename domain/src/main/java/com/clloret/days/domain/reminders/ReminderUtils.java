package com.clloret.days.domain.reminders;

import java.util.Date;

public interface ReminderUtils {

  void addReminder(String id, String message, Date date);

  void removeReminder(String id);

  boolean isActive(String id);
}
