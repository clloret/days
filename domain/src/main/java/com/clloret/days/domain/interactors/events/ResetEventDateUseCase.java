package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import com.clloret.days.domain.utils.TimeProvider;
import io.reactivex.Maybe;
import javax.inject.Inject;
import org.joda.time.LocalDate;

public class ResetEventDateUseCase extends BaseMaybeUseCase<Event, Event> {

  private final EventRepository dataStore;
  private final TimeProvider timeProvider;
  private final EventReminderManager eventReminderManager;

  @Inject
  public ResetEventDateUseCase(ThreadSchedulers threadSchedulers, EventRepository dataStore,
      TimeProvider timeProvider, EventReminderManager eventReminderManager) {

    super(threadSchedulers);

    this.dataStore = dataStore;
    this.timeProvider = timeProvider;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  protected Maybe<Event> buildUseCaseObservable(Event event) {

    if (event == null) {
      return Maybe.error(new IllegalArgumentException("The event cannot be null"));
    }

    LocalDate date = timeProvider.getCurrentDate();
    event.setDate(date.toDate());

    return dataStore.edit(event)
        .doOnSuccess(this::reminderSchedule);
  }

  private void reminderSchedule(Event event) {

    if (event.hasReminder()) {
      eventReminderManager.scheduleReminder(event, true);
    }
  }

}
