package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Maybe;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateEventUseCase implements
    MaybeUseCaseWithParameter<Event, Event> {

  private final EventRepository dataStore;
  private final EventReminderManager eventReminderManager;

  @Inject
  public CreateEventUseCase(EventRepository dataStore, EventReminderManager eventReminderManager) {

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  public Maybe<Event> execute(Event event) {

    return dataStore.create(event)
        .doOnSuccess(this::reminderSchedule);
  }

  private void reminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventReminderManager.scheduleReminder(event, false);
    }
  }

}
