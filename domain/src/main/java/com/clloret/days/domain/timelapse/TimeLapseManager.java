package com.clloret.days.domain.timelapse;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.utils.TimeProvider;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeLapseManager {

  private static final int AIRTABLE_DELAY = 200;
  private final Logger logger = LoggerFactory
      .getLogger(TimeLapseManager.class.getSimpleName());
  private final TimeProvider timeProvider;
  private final EventReminderManager eventReminderManager;
  private final AppDataStore appDataStore;

  public TimeLapseManager(TimeProvider timeProvider,
      EventReminderManager eventReminderManager,
      AppDataStore appDataStore) {

    this.timeProvider = timeProvider;
    this.eventReminderManager = eventReminderManager;
    this.appDataStore = appDataStore;
  }

  Event updateEventDateFromTimeLapse(Event event) {

    if (!isTimeLapseActiveAndValid(event)) {
      return event;
    }

    return calculateNewEventDate(event);
  }

  private boolean isTimeLapseActiveAndValid(Event event) {

    LocalDate eventDate = new LocalDate(event.getDate());
    int timeLapse = event.getTimeLapse();

    return timeLapse > 0 && !timeProvider.getCurrentDate().isBefore(eventDate);
  }

  private Event calculateNewEventDate(Event event) {

    LocalDate eventDate = new LocalDate(event.getDate());
    int timeLapse = event.getTimeLapse();

    LocalDate newEventDate;
    switch (event.getTimeLapseUnit()) {
      case DAY:
      default:
        newEventDate = eventDate.plusDays(timeLapse);
        break;
      case MONTH:
        newEventDate = eventDate.plusMonths(timeLapse);
        break;
      case YEAR:
        newEventDate = eventDate.plusYears(timeLapse);
        break;
    }

    Event newEvent = event.clone();
    newEvent.setDate(newEventDate.toDate());

    return newEvent;
  }

  private Maybe<Event> updateEventAndSave(Event event) {

    logger.debug("updateEventAndSave: {}", event.getName());

    Event newEvent = updateEventDateFromTimeLapse(event);

    if (Objects.equals(event.getDate(), newEvent.getDate())) {
      return Maybe.just(event);
    }

    return appDataStore
        .editEvent(newEvent)
        .delay(AIRTABLE_DELAY, TimeUnit.MILLISECONDS)
        .doOnSuccess(result -> eventReminderManager.scheduleReminder(result, false));
  }

  public Observable<Event> updateEventsDatesFromTimeLapse() {

    logger.debug("updateEventsDatesFromTimeLapse");

    return appDataStore.getEvents(true)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .flatMap(event -> updateEventAndSave(event).toObservable());
  }

}
