package com.clloret.days.model.entities.mapper;

import androidx.annotation.NonNull;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.model.entities.EventViewModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventViewModelMapper {

  @Inject
  public EventViewModelMapper() {

  }

  public Event toEvent(@NonNull EventViewModel entity) {

    return new Event(
        entity.getId(),
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

  public EventViewModel fromEvent(@NonNull Event entity) {

    return new EventViewModel(
        entity.getId(),
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

  public List<EventViewModel> fromEvent(Collection<Event> entityCollection) {

    final List<EventViewModel> mappedList = new ArrayList<>(20);
    for (Event event : entityCollection) {
      final EventViewModel mappedEntity = fromEvent(event);
      if (mappedEntity != null) {
        mappedList.add(mappedEntity);
      }
    }
    return mappedList;
  }

}
