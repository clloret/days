package com.clloret.days.domain.reminders;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.utils.Optional;
import java.util.Date;

public interface ReminderManager {

  void addReminder(Event event, String id, Date date, String contentTitle, String contentText,
      Optional<String> bigText);

  void removeReminderForEvent(Event event);

}
