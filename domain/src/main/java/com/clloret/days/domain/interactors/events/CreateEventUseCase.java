package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.EventProgressCalculator;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class CreateEventUseCase extends BaseMaybeUseCase<Event, Event> {

  private final EventRepository dataStore;
  private final EventReminderManager eventReminderManager;
  private final EventProgressCalculator eventProgressCalculator;

  @Inject
  public CreateEventUseCase(
      ThreadSchedulers threadSchedulers,
      EventRepository dataStore,
      EventReminderManager eventReminderManager,
      EventProgressCalculator eventProgressCalculator) {

    super(threadSchedulers);

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
    this.eventProgressCalculator = eventProgressCalculator;
  }

  @Override
  protected Maybe<Event> buildUseCaseObservable(Event event) {

    eventProgressCalculator.setDefaultProgressDate(event);

    return dataStore.create(event)
        .doOnSuccess(this::reminderSchedule);
  }

  private void reminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventReminderManager.scheduleReminder(event, false);
    }
  }

}
