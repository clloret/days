package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventRemindersManager;
import io.reactivex.Maybe;

public class DeleteEventUseCase implements MaybeUseCaseWithParameter<Event, Boolean> {

  private final AppDataStore dataStore;
  private final EventRemindersManager eventRemindersManager;

  public DeleteEventUseCase(AppDataStore dataStore, EventRemindersManager eventRemindersManager) {

    this.dataStore = dataStore;
    this.eventRemindersManager = eventRemindersManager;
  }

  @Override
  public Maybe<Boolean> execute(Event event) {

    return dataStore.deleteEvent(event)
        .doOnSuccess(deleted -> removeReminderSchedule(event));
  }

  private void removeReminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventRemindersManager.scheduleReminder(event, false, true);
    }
  }

}
