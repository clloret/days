package com.clloret.days.data.remote.entities.mapper;

import com.clloret.days.data.remote.entities.ApiEvent;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApiEventDataMapper {

  public Event toEvent(ApiEvent apiEvent) {

    Event event = null;
    if (apiEvent != null) {
      event = new EventBuilder()
          .setId(apiEvent.getId())
          .setName(apiEvent.getName())
          .setDescription(apiEvent.getDescription())
          .setDate(apiEvent.getDate())
          .setFavorite(apiEvent.isFavorite())
          .setTags(apiEvent.getTags())
          .build();
    }
    return event;
  }

  public List<Event> toEvent(Collection<ApiEvent> apiEventCollection) {

    final List<Event> eventList = new ArrayList<>(20);
    for (ApiEvent dbEvent : apiEventCollection) {
      final Event event = toEvent(dbEvent);
      if (event != null) {
        eventList.add(event);
      }
    }
    return eventList;
  }

  public ApiEvent fromEvent(Event event, boolean copyId) {

    ApiEvent apiEvent = null;
    if (event != null) {
      apiEvent = new ApiEvent(copyId ? event.getId() : null);
      apiEvent.setName(event.getName());
      apiEvent.setDescription(event.getDescription());
      apiEvent.setDate(event.getDate());
      apiEvent.setFavorite(event.isFavorite());
      apiEvent.setTags(event.getTags());
    }
    return apiEvent;
  }

  public List<ApiEvent> fromEvent(Collection<Event> eventCollection) {

    final List<ApiEvent> eventList = new ArrayList<>(20);
    for (Event event : eventCollection) {
      final ApiEvent apiEvent = fromEvent(event, true);
      if (apiEvent != null) {
        eventList.add(apiEvent);
      }
    }
    return eventList;
  }

}
