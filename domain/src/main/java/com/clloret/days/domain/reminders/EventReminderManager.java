package com.clloret.days.domain.reminders;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.TimeProvider;
import java.util.Collection;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class EventReminderManager {

  private final ReminderManager reminderManager;
  private final TimeProvider timeProvider;
  private final PreferenceUtils preferenceUtils;

  public EventReminderManager(ReminderManager reminderManager, TimeProvider timeProvider,
      PreferenceUtils preferenceUtils) {

    this.reminderManager = reminderManager;
    this.timeProvider = timeProvider;
    this.preferenceUtils = preferenceUtils;
  }

  private void addReminderForEvent(Event event) {

    if (!event.hasReminder()) {
      return;
    }

    DateTime eventDateWithTime = getEventDateWithTime(event);
    DateTime timeReminder = calculateTimeReminder(event, eventDateWithTime);

    if (isReminderInThePast(timeReminder)) {
      return;
    }

    reminderManager.addReminder(event, event.getId(), event.getName(), timeReminder.toDate());
  }

  private boolean isReminderInThePast(DateTime eventDateWithTime) {

    DateTime dateTime = timeProvider.getCurrentTime();

    return eventDateWithTime.isBefore(dateTime);
  }

  private DateTime getEventDateWithTime(Event event) {

    int reminderTime = preferenceUtils.getReminderTime();
    int hourOfDay = reminderTime / DateTimeConstants.MINUTES_PER_HOUR;
    int minuteOfHour = reminderTime % DateTimeConstants.MINUTES_PER_HOUR;

    return new DateTime(event.getDate())
        .withHourOfDay(hourOfDay)
        .withMinuteOfHour(minuteOfHour);
  }

  private DateTime calculateTimeReminder(Event event, DateTime eventDateWithTime) {

    DateTime timeReminder;
    switch (event.getReminderUnit()) {
      case DAY:
      default:
        timeReminder = eventDateWithTime.plusDays(event.getReminder());
        break;
      case MONTH:
        timeReminder = eventDateWithTime.plusMonths(event.getReminder());
        break;
      case YEAR:
        timeReminder = eventDateWithTime.plusYears(event.getReminder());
        break;
    }

    return timeReminder;
  }

  public void scheduleReminder(Event event, boolean removePreviously) {

    if (removePreviously) {
      reminderManager.removeReminderForEvent(event);
    }
    addReminderForEvent(event);
  }

  public void removeReminder(Event event) {

    reminderManager.removeReminderForEvent(event);
  }

  public void scheduleReminders(Collection<Event> events, boolean removePreviously) {

    for (Event event : events) {
      if (removePreviously) {
        reminderManager.removeReminderForEvent(event);
      }
      addReminderForEvent(event);
    }
  }

}
