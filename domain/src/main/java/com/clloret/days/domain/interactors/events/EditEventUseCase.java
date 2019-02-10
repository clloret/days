package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.events.EditEventUseCase.RequestValues;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventRemindersManager;
import io.reactivex.Maybe;
import java.util.Objects;

public class EditEventUseCase implements MaybeUseCaseWithParameter<RequestValues, Event> {

  private final AppDataStore dataStore;
  private final EventRemindersManager eventRemindersManager;

  public EditEventUseCase(AppDataStore dataStore,
      EventRemindersManager eventRemindersManager) {

    this.dataStore = dataStore;
    this.eventRemindersManager = eventRemindersManager;
  }

  @Override
  public Maybe<Event> execute(RequestValues requestValues) {

    final Event modifiedEvent = requestValues.modifiedEvent;
    final Event originalEvent = requestValues.originalEvent;
    boolean scheduleReminder = false;

    if (!Objects.equals(modifiedEvent.getReminder(), originalEvent.getReminder())) {
      scheduleReminder = true;
    }

    if (modifiedEvent.hasReminder() && (modifiedEvent.getDate() != originalEvent.getDate())) {
      scheduleReminder = true;
    }

    final boolean finalScheduleReminder = scheduleReminder;

    return dataStore.editEvent(modifiedEvent)
        .doOnSuccess(
            event -> reminderSchedule(event, finalScheduleReminder, modifiedEvent.hasReminder(),
                originalEvent.hasReminder()));
  }

  private void reminderSchedule(Event event, boolean schedule, boolean add,
      boolean removePreviously) {

    if (schedule) {
      eventRemindersManager.scheduleReminder(event, add, removePreviously);
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
