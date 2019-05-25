package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class DeleteEventUseCase extends BaseMaybeUseCase<Event, Boolean> {

  private final EventRepository dataStore;
  private final EventReminderManager eventReminderManager;

  @Inject
  public DeleteEventUseCase(ThreadSchedulers threadSchedulers, EventRepository dataStore,
      EventReminderManager eventReminderManager) {

    super(threadSchedulers);

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  protected Maybe<Boolean> buildUseCaseObservable(Event event) {

    return dataStore.delete(event)
        .doOnSuccess(deleted -> removeReminderSchedule(event));
  }

  private void removeReminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventReminderManager.removeReminder(event);
    }
  }

}
