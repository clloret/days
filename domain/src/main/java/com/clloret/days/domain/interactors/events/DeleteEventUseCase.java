package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventReminderManager;
import io.reactivex.Maybe;

public class DeleteEventUseCase implements MaybeUseCaseWithParameter<Event, Boolean> {

  private final AppDataStore dataStore;
  private final EventReminderManager eventReminderManager;

  public DeleteEventUseCase(AppDataStore dataStore, EventReminderManager eventReminderManager) {

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  public Maybe<Boolean> execute(Event event) {

    return dataStore.deleteEvent(event)
        .doOnSuccess(deleted -> removeReminderSchedule(event));
  }

  private void removeReminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventReminderManager.removeReminder(event);
    }
  }

}
