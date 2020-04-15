package com.clloret.days.data.local.entities.mapper;

import androidx.annotation.NonNull;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.remote.entities.mapper.DataMapper;
import com.clloret.days.domain.entities.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbEventDataMapper implements DataMapper<Event, DbEvent> {

  @Inject
  public DbEventDataMapper() {

  }

  @Override
  public Event toEntity(@NonNull DbEvent entity) {

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

  @Override
  public List<Event> toEntity(@NonNull Collection<DbEvent> entityCollection) {

    final List<Event> eventList = new ArrayList<>(20);
    for (DbEvent dbEvent : entityCollection) {
      final Event event = toEntity(dbEvent);
      if (event != null) {
        eventList.add(event);
      }
    }
    return eventList;
  }

  @Override
  public DbEvent fromEntity(@NonNull Event entity, boolean copyId) {

    return new DbEvent(
        Objects.requireNonNull(entity.getId(), "Entity ID can't be null"),
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
  public List<DbEvent> fromEntity(@NonNull Collection<Event> entityCollection) {

    final List<DbEvent> eventList = new ArrayList<>(20);
    for (Event event : entityCollection) {
      final DbEvent dbEvent = fromEntity(event, true);
      if (dbEvent != null) {
        eventList.add(dbEvent);
      }
    }
    return eventList;
  }
}
