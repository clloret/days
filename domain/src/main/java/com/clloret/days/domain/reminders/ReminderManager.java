package com.clloret.days.domain.reminders;

import com.clloret.days.domain.entities.Event;
import java.util.Date;

public interface ReminderManager {

  void addReminder(Event event, String id, String message, Date date);

  void removeReminderForEvent(Event event);

}
