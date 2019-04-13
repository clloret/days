package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.utils.TimeProvider;
import io.reactivex.Maybe;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.LocalDate;

@Singleton
public class ResetEventDateUseCase implements MaybeUseCaseWithParameter<Event, Event> {

  private final AppDataStore dataStore;
  private final TimeProvider timeProvider;
  private final EventReminderManager eventReminderManager;

  @Inject
  public ResetEventDateUseCase(AppDataStore dataStore, TimeProvider timeProvider,
      EventReminderManager eventReminderManager) {

    this.dataStore = dataStore;
    this.timeProvider = timeProvider;
    this.eventReminderManager = eventReminderManager;
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
      eventReminderManager.scheduleReminder(event, true);
    }
  }
}
