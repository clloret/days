package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.filter.EventFilterStrategy;
import com.clloret.days.domain.interactors.events.GetFilteredEventsUseCase.RequestValues;
import com.clloret.days.domain.interactors.types.SingleUseCaseWithParameter;
import com.clloret.days.domain.reminders.EventReminderManager;
import io.reactivex.Single;
import java.util.List;

public class GetFilteredEventsUseCase implements
    SingleUseCaseWithParameter<RequestValues, List<Event>> {

  private final AppDataStore dataStore;
  private final EventReminderManager eventReminderManager;

  public GetFilteredEventsUseCase(AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    this.dataStore = dataStore;
    this.eventReminderManager = eventReminderManager;
  }

  @Override
  public Single<List<Event>> execute(RequestValues requestValues) {

    return requestValues.filterStrategy.getEvents(dataStore)
        .doOnSuccess(events -> reminderScheduleList(events, requestValues.scheduleReminders));
  }

  private void reminderScheduleList(List<Event> events, boolean scheduleReminders) {

    if (scheduleReminders) {
      eventReminderManager.scheduleReminders(events, true);
    }
  }

  public static final class RequestValues {

    private final EventFilterStrategy filterStrategy;
    private final boolean scheduleReminders;

    public RequestValues(EventFilterStrategy filterStrategy, boolean scheduleReminders) {

      this.filterStrategy = filterStrategy;
      this.scheduleReminders = scheduleReminders;
    }
  }

}
