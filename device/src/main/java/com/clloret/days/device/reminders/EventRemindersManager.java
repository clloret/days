package com.clloret.days.device.reminders;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import com.clloret.days.device.R;
import com.clloret.days.device.events.ReminderAllScheduleEvent;
import com.clloret.days.device.events.ReminderListScheduleEvent;
import com.clloret.days.device.events.ReminderScheduleEvent;
import com.clloret.days.device.notifications.NotificationsFactory;
import com.clloret.days.device.notifications.NotificationsUtils;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Objects;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class EventRemindersManager {

  private static final int DEFAULT_REMINDER_TIME = 0;
  private final RemindersUtils remindersUtils;
  private final SharedPreferences preferences;
  private final Resources resources;
  private final AppDataStore appDataStore;

  public EventRemindersManager(Context context, Class<?> cls, SharedPreferences preferences,
      AppDataStore appDataStore, EventBus eventBus) {

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

    eventBus.register(this);
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

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(ReminderScheduleEvent event) {

    if (event.add) {
      if (event.removePreviously) {
        removeReminderForEvent(event.event);
      }
      addReminderForEvent(event.event);
    } else {
      removeReminderForEvent(event.event);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(ReminderListScheduleEvent event) {

    if (event.add) {
      addRemindersForEvents(event.events, true);
    } else {
      removeRemindersForEvents(event.events);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(ReminderAllScheduleEvent event) {

    scheduleAllEvents();
  }

}
