package com.clloret.days.domain.interactors.events;

import static com.clloret.days.domain.entities.Event.REMINDER_EVENT_DAY;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventRemindersManager;
import io.reactivex.Maybe;

public class ToggleEventReminderUseCase implements MaybeUseCaseWithParameter<Event, Event> {

  private final AppDataStore dataStore;
  private final EventRemindersManager eventRemindersManager;

  public ToggleEventReminderUseCase(AppDataStore dataStore,
      EventRemindersManager eventRemindersManager) {

    this.dataStore = dataStore;
    this.eventRemindersManager = eventRemindersManager;
  }

  @Override
  public Maybe<Event> execute(Event event) {

    final boolean removePreviously = event.hasReminder();

    if (event.hasReminder()) {
      event.setReminder(null);
    } else {
      event.setReminder(REMINDER_EVENT_DAY);
      event.setReminderUnit(Event.TimeUnit.DAY);
    }

    return dataStore.editEvent(event)
        .doOnSuccess(result -> reminderSchedule(result, removePreviously));
  }

  private void reminderSchedule(Event event, boolean removePreviously) {

    eventRemindersManager.scheduleReminder(event, event.hasReminder(), removePreviously);
  }

}
