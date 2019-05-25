package com.clloret.days.domain.interactors.events;

import static com.clloret.days.domain.entities.Event.REMINDER_EVENT_DAY;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class ToggleEventReminderUseCase extends BaseMaybeUseCase<Event, Event> {

  private final EventRepository dataStore;
  private final EventReminderManager eventReminderManager;

  @Inject
  public ToggleEventReminderUseCase(ThreadSchedulers threadSchedulers, EventRepository dataStore,
      EventReminderManager eventReminderManager) {

    super(threadSchedulers);

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  protected Maybe<Event> buildUseCaseObservable(Event event) {

    if (event.hasReminder()) {
      event.setReminder(null);
    } else {
      event.setReminder(REMINDER_EVENT_DAY);
      event.setReminderUnit(Event.TimeUnit.DAY);
    }

    return dataStore.edit(event)
        .doOnSuccess(this::reminderSchedule);
  }

  private void reminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventReminderManager.scheduleReminder(event, false);
    } else {
      eventReminderManager.removeReminder(event);
    }
  }

}
