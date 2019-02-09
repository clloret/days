package com.clloret.days.domain.reminders;

import com.clloret.days.domain.entities.Event;
import java.util.List;

public interface EventRemindersManager {

  void scheduleReminder(Event event, boolean add, boolean removePreviously);

  void scheduleReminderList(List<Event> events, boolean add);

  void scheduleReminderAll();
}
