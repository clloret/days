package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventRemindersManager;
import com.clloret.days.domain.utils.TimeProvider;
import io.reactivex.Maybe;
import org.joda.time.LocalDate;

public class ResetEventDateUseCase implements MaybeUseCaseWithParameter<Event, Event> {

  private final AppDataStore dataStore;
  private final TimeProvider timeProvider;
  private final EventRemindersManager eventRemindersManager;

  public ResetEventDateUseCase(AppDataStore dataStore,
      TimeProvider timeProvider,
      EventRemindersManager eventRemindersManager) {

    this.dataStore = dataStore;
    this.timeProvider = timeProvider;
    this.eventRemindersManager = eventRemindersManager;
  }

  @Override
  public Maybe<Event> execute(Event event) {

    LocalDate date = timeProvider.getCurrentDate();
    event.setDate(date.toDate());

    return dataStore.editEvent(event)
        .doOnSuccess(this::reminderSchedule);
  }

  private void reminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventRemindersManager.scheduleReminder(event, true, true);
    }
  }
}
