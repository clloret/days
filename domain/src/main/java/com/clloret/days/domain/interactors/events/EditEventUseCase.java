package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.events.EditEventUseCase.RequestValues;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Maybe;
import java.util.Objects;
import javax.inject.Inject;

public class EditEventUseCase implements MaybeUseCaseWithParameter<RequestValues, Event> {

  private final EventRepository dataStore;
  private final EventReminderManager eventReminderManager;

  @Inject
  public EditEventUseCase(EventRepository dataStore,
      EventReminderManager eventReminderManager) {

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  public Maybe<Event> execute(RequestValues requestValues) {

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

//    Completable completable = dataStore.editEvent(modifiedEvent)
//        .flatMapCompletable(event -> Completable.fromAction(
//            () -> reminderSchedule(event, finalScheduleReminder, originalEvent.hasReminder())));

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
