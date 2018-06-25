package com.clloret.days.model.local.entities.mapper;

import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.EventBuilder;
import com.clloret.days.model.local.entities.DbEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DbEventDataMapper {

  private Event toEvent(DbEvent dbEvent) {

    Event event = null;
    if (dbEvent != null) {
      event = new EventBuilder()
          .setId(dbEvent.getId())
          .setName(dbEvent.getName())
          .setDescription(dbEvent.getDescription())
          .setDate(dbEvent.getDate())
          .setFavorite(dbEvent.isFavorite())
          .setTags(dbEvent.getTags())
          .build();
    }
    return event;
  }

  public List<Event> toEvent(Collection<DbEvent> dbEventCollection) {

    final List<Event> eventList = new ArrayList<>(20);
    for (DbEvent dbEvent : dbEventCollection) {
      final Event event = toEvent(dbEvent);
      if (event != null) {
        eventList.add(event);
      }
    }
    return eventList;
  }

  public DbEvent fromEvent(Event event) {

    DbEvent dbEvent = null;
    if (event != null) {
      dbEvent = new DbEvent(event.getId());
      dbEvent.setName(event.getName());
      dbEvent.setDescription(event.getDescription());
      dbEvent.setDate(event.getDate());
      dbEvent.setFavorite(event.isFavorite());
      dbEvent.setTags(event.getTags());
    }
    return dbEvent;
  }

  public List<DbEvent> fromEvent(Collection<Event> eventCollection) {

    final List<DbEvent> eventList = new ArrayList<>(20);
    for (Event event : eventCollection) {
      final DbEvent dbEvent = fromEvent(event);
      if (dbEvent != null) {
        eventList.add(dbEvent);
      }
    }
    return eventList;
  }
}
