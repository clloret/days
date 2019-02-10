package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventRemindersManager;
import io.reactivex.Maybe;

public class CreateEventUseCase implements MaybeUseCaseWithParameter<Event, Event> {

  private final AppDataStore dataStore;
  private final EventRemindersManager eventRemindersManager;

  public CreateEventUseCase(AppDataStore dataStore,
      EventRemindersManager eventRemindersManager) {

    this.dataStore = dataStore;
    this.eventRemindersManager = eventRemindersManager;
  }

  @Override
  public Maybe<Event> execute(Event event) {

    return dataStore.createEvent(event)
        .doOnSuccess(this::reminderSchedule);
  }

  private void reminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventRemindersManager.scheduleReminder(event, true, false);
    }
  }

}
