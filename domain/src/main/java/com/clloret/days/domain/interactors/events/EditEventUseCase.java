package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.interactors.events.EditEventUseCase.RequestValues;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;
import java.util.Objects;
import javax.inject.Inject;

public class EditEventUseCase extends BaseMaybeUseCase<RequestValues, Event> {

  private final EventRepository dataStore;
  private final EventReminderManager eventReminderManager;

  @Inject
  public EditEventUseCase(ThreadSchedulers threadSchedulers, EventRepository dataStore,
      EventReminderManager eventReminderManager) {

    super(threadSchedulers);

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  protected Maybe<Event> buildUseCaseObservable(RequestValues requestValues) {

    final Event modifiedEvent = requestValues.modifiedEvent;
    final Event originalEvent = requestValues.originalEvent;
    boolean scheduleReminder = false;

    if (modifiedEvent.hasReminder() && isRemindersNotEquals(modifiedEvent, originalEvent)) {
      scheduleReminder = true;
    }

    if (modifiedEvent.hasReminder() && isDatesNotEquals(modifiedEvent, originalEvent)) {
      scheduleReminder = true;
    }

    final boolean finalScheduleReminder = scheduleReminder;

    return dataStore.edit(modifiedEvent)
        .doOnSuccess(
            event -> reminderSchedule(event, finalScheduleReminder, originalEvent.hasReminder()));
  }

  private boolean isDatesNotEquals(Event modifiedEvent, Event originalEvent) {

    return !Objects.equals(modifiedEvent.getDate(), originalEvent.getDate());
  }

  private boolean isRemindersNotEquals(Event modifiedEvent, Event originalEvent) {

    return !Objects.equals(modifiedEvent.getReminder(), originalEvent.getReminder());
  }

  private void reminderSchedule(Event event, boolean schedule, boolean removePreviously) {

    if (schedule) {
      eventReminderManager.scheduleReminder(event, removePreviously);
    } else if (removePreviously) {
      eventReminderManager.removeReminder(event);
    }
  }

  public static final class RequestValues {

    private final Event modifiedEvent;
    private final Event originalEvent;

    public RequestValues(Event modifiedEvent, Event originalEvent) {

      this.modifiedEvent = modifiedEvent;
      this.originalEvent = originalEvent;
    }
  }

}
