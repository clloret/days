package com.clloret.days.device.reminders;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import com.clloret.days.device.R;
import com.clloret.days.device.notifications.NotificationsFactory;
import com.clloret.days.device.notifications.NotificationsUtils;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.reminders.EventRemindersManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import timber.log.Timber;

public class EventRemindersManagerImpl implements EventRemindersManager {

  private static final int DEFAULT_REMINDER_TIME = 0;
  private final RemindersUtils remindersUtils;
  private final SharedPreferences preferences;
  private final Resources resources;
  private final AppDataStore appDataStore;

  public EventRemindersManagerImpl(Context context, Class<?> cls, SharedPreferences preferences,
      AppDataStore appDataStore) {

    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationsUtils notificationsUtils = new NotificationsUtils(
        Objects.requireNonNull(notificationManager));
    NotificationsFactory notificationsFactory = new NotificationsFactory(context,
        context.getResources(),
        notificationsUtils);

    this.remindersUtils = new RemindersUtils(notificationsFactory, context, cls);
    this.preferences = preferences;
    this.resources = context.getResources();
    this.appDataStore = appDataStore;
  }

  private void addReminderForEvent(Event event) {

    int reminderTime = preferences.getInt(resources.getString(R.string.pref_reminder_time),
        DEFAULT_REMINDER_TIME);
    int hourOfDay = reminderTime / DateTimeConstants.MINUTES_PER_HOUR;
    int minuteOfHour = reminderTime % DateTimeConstants.MINUTES_PER_HOUR;

    DateTime eventDateWithTime = new DateTime(event.getDate()).withHourOfDay(hourOfDay)
        .withMinuteOfHour(minuteOfHour);
    if (!event.hasReminder() || eventDateWithTime.isBeforeNow()) {
      return;
    }

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

    remindersUtils.addReminder(event.getId(), event.getName(), timeReminder.toDate());
  }

  private void addRemindersForEvents(List<Event> events, boolean removePreviously) {

    for (Event event : events) {
      if (removePreviously) {
        removeReminderForEvent(event);
      }
      addReminderForEvent(event);
    }
  }

  private void removeReminderForEvent(Event event) {

    remindersUtils.removeReminder(event.getId());
  }

  private void removeRemindersForEvents(List<Event> events) {

    for (Event event : events) {
      removeReminderForEvent(event);
    }
  }

  private void scheduleAllEvents() {

    appDataStore.getEvents(true)
        .doOnSuccess(list -> addRemindersForEvents(list, false))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe();
  }

  @Override
  public void scheduleReminder(Event event, boolean add, boolean removePreviously) {

    Timber.d("scheduleReminder");

    if (add) {
      if (removePreviously) {
        removeReminderForEvent(event);
      }
      addReminderForEvent(event);
    } else {
      removeReminderForEvent(event);
    }
  }

  @Override
  public void scheduleReminderList(List<Event> events, boolean add) {

    Timber.d("scheduleReminderList");

    if (add) {
      addRemindersForEvents(events, true);
    } else {
      removeRemindersForEvents(events);
    }
  }

  @Override
  public void scheduleReminderAll() {

    Timber.d("scheduleReminderAll");

    scheduleAllEvents();
  }
}
