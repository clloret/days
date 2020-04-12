package com.clloret.days.data.remote.entities.mapper;

import com.clloret.days.data.remote.entities.ApiEvent;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApiEventDataMapper implements DataMapper<Event, ApiEvent> {

  @Inject
  public ApiEventDataMapper() {

  }

  @Override
  public Event toEntity(ApiEvent model) {

    Event event = null;
    if (model != null) {
      event = new EventBuilder()
          .setId(model.getId())
          .setName(model.getName())
          .setDescription(model.getDescription())
          .setDate(model.getDate())
          .setFavorite(model.getFavorite())
          .setTags(model.getTags())
          .setReminder(model.getReminder())
          .setReminderUnit(model.getReminderTimeUnit())
          .setTimeLapse(model.getTimeLapse())
          .setTimeLapseUnit(model.getTimeLapseTimeUnit())
          .build();
    }
    return event;
  }

  @Override
  public List<Event> toEntity(Collection<ApiEvent> modelCollection) {

    final List<Event> eventList = new ArrayList<>(20);
    for (ApiEvent dbEvent : modelCollection) {
      final Event event = toEntity(dbEvent);
      if (event != null) {
        eventList.add(event);
      }
    }
    return eventList;
  }

  @Override
  public ApiEvent fromEntity(Event model, boolean copyId) {

    ApiEvent apiEvent = null;
    if (model != null) {
      apiEvent = new ApiEvent(copyId ? model.getId() : null);
      apiEvent.setName(model.getName());
      apiEvent.setDescription(model.getDescription());
      apiEvent.setDate(model.getDate());
      apiEvent.setFavorite(model.getFavorite());
      apiEvent.setTags(model.getTags());
      apiEvent.setReminder(model.getReminder());
      apiEvent.setReminderTimeUnit(model.getReminderUnit());
      apiEvent.setTimeLapse(model.getTimeLapse());
      apiEvent.setTimeLapseTimeUnit(model.getTimeLapseUnit());
    }
    return apiEvent;
  }

  @Override
  public List<ApiEvent> fromEntity(Collection<Event> modelCollection) {

    final List<ApiEvent> eventList = new ArrayList<>(20);
    for (Event event : modelCollection) {
      final ApiEvent apiEvent = fromEntity(event, true);
      if (apiEvent != null) {
        eventList.add(apiEvent);
      }
    }
    return eventList;
  }

}
