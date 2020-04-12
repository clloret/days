package com.clloret.days.data.remote.entities.mapper;

import androidx.annotation.NonNull;
import com.clloret.days.data.remote.entities.ApiEvent;
import com.clloret.days.domain.entities.Event;
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
  public Event toEntity(@NonNull ApiEvent entity) {

    return new Event(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getDate(),
        entity.getTags(),
        entity.getFavorite(),
        entity.getReminder(),
        entity.getReminderTimeUnit(),
        entity.getTimeLapse(),
        entity.getTimeLapseTimeUnit(),
        entity.getProgressDate()
    );
  }

  @Override
  public List<Event> toEntity(@NonNull Collection<ApiEvent> entityCollection) {

    final List<Event> eventList = new ArrayList<>(20);
    for (ApiEvent dbEvent : entityCollection) {
      final Event event = toEntity(dbEvent);
      if (event != null) {
        eventList.add(event);
      }
    }
    return eventList;
  }

  @Override
  public ApiEvent fromEntity(@NonNull Event entity, boolean copyId) {

    return new ApiEvent(
        copyId ? entity.getId() : null,
        entity.getName(),
        entity.getDescription(),
        entity.getDate(),
        entity.getTags(),
        entity.getFavorite(),
        entity.getReminder(),
        entity.getReminderUnit(),
        entity.getTimeLapse(),
        entity.getTimeLapseUnit(),
        entity.getProgressDate()
    );

  }

  @Override
  public List<ApiEvent> fromEntity(@NonNull Collection<Event> entityCollection) {

    final List<ApiEvent> eventList = new ArrayList<>(20);
    for (Event event : entityCollection) {
      final ApiEvent apiEvent = fromEntity(event, true);
      if (apiEvent != null) {
        eventList.add(apiEvent);
      }
    }
    return eventList;
  }

}
