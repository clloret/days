package com.clloret.days.data.local.entities.mapper;

import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
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
          .setReminder(dbEvent.getReminder())
          .setReminderUnit(dbEvent.getReminderUnit())
          .setTimeLapse(dbEvent.getTimeLapse())
          .setTimeLapseUnit(dbEvent.getTimeLapseUnit())
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
      dbEvent.setReminder(event.getReminder());
      dbEvent.setReminderUnit(event.getReminderUnit());
      dbEvent.setTimeLapse(event.getTimeLapse());
      dbEvent.setTimeLapseUnit(event.getTimeLapseUnit());
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
