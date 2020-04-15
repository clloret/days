package com.clloret.days.domain.reminders;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.EventPeriodFormat;
import com.clloret.days.domain.utils.Optional;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.StringUtils;
import com.clloret.days.domain.utils.TimeProvider;
import java.util.Collection;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class EventReminderManager {

  private final Logger logger = LoggerFactory
      .getLogger(EventReminderManager.class.getSimpleName());
  private final ReminderManager reminderManager;
  private final TimeProvider timeProvider;
  private final PreferenceUtils preferenceUtils;
  private final EventPeriodFormat eventPeriodFormat;
  private final StringResourceProvider stringResourceProvider;

  @Inject
  public EventReminderManager(ReminderManager reminderManager, TimeProvider timeProvider,
      PreferenceUtils preferenceUtils, EventPeriodFormat eventPeriodFormat,
      StringResourceProvider stringResourceProvider) {

    this.reminderManager = reminderManager;
    this.timeProvider = timeProvider;
    this.preferenceUtils = preferenceUtils;
    this.eventPeriodFormat = eventPeriodFormat;
    this.stringResourceProvider = stringResourceProvider;
  }

  private void addReminderForEvent(Event event) {

    if (!event.hasReminder()) {
      return;
    }

    final DateTime eventDateWithTime = getEventDateWithTime(event);
    final DateTime timeReminder = calculateTimeReminder(event, eventDateWithTime);
    final Date dateReminder = timeReminder.toLocalDate().toDate();

    logger.debug("dateReminder: {}", dateReminder.toString());

    if (isReminderInThePast(timeReminder)) {
      return;
    }

    final String contentTitle = event.getName();
    final String contentText = eventPeriodFormat
        .getTimeLapseFormatted(event.getDate(), dateReminder, PeriodType.days());
    final String notificationBigText = stringResourceProvider.getNotificationBigText();
    final String eventDescription = event.getDescription();
    final Optional<String> bigText = getBigTextFromEventDescription(contentText,
        notificationBigText, eventDescription);

    reminderManager
        .addReminder(event, event.getId(), timeReminder.toDate(), contentTitle, contentText,
            bigText);
  }

  private Optional<String> getBigTextFromEventDescription(String contentText,
      String notificationBigText,
      String eventDescription) {

    return StringUtils.isNullOrEmpty(eventDescription) ? Optional.empty()
        : Optional.of(String.format(notificationBigText, contentText, eventDescription));
  }

  private boolean isReminderInThePast(DateTime eventDateWithTime) {

    final DateTime dateTime = timeProvider.getCurrentTime();

    return eventDateWithTime.isBefore(dateTime);
  }

  private DateTime getEventDateWithTime(Event event) {

    final int reminderTime = preferenceUtils.getReminderTime();
    final int hourOfDay = reminderTime / DateTimeConstants.MINUTES_PER_HOUR;
    final int minuteOfHour = reminderTime % DateTimeConstants.MINUTES_PER_HOUR;

    return new DateTime(event.getDate())
        .withHourOfDay(hourOfDay)
        .withMinuteOfHour(minuteOfHour);
  }

  private DateTime calculateTimeReminder(Event event, DateTime eventDateWithTime) {

    Integer reminder = event.getReminder();

    if (reminder == null) {
      throw new IllegalArgumentException("Event reminder can't be null");
    }

    DateTime timeReminder;
    switch (event.getReminderUnit()) {
      case DAY:
      default:
        timeReminder = eventDateWithTime.plusDays(reminder);
        break;
      case MONTH:
        timeReminder = eventDateWithTime.plusMonths(reminder);
        break;
      case YEAR:
        timeReminder = eventDateWithTime.plusYears(reminder);
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
