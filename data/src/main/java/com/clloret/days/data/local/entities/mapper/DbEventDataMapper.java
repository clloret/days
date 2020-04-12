package com.clloret.days.data.local.entities.mapper;

import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.remote.entities.mapper.DataMapper;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbEventDataMapper implements DataMapper<Event, DbEvent> {

  @Inject
  public DbEventDataMapper() {

  }

  @Override
  public Event toEntity(DbEvent model) {

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
          .setReminderUnit(model.getReminderUnit())
          .setTimeLapse(model.getTimeLapse())
          .setTimeLapseUnit(model.getTimeLapseUnit())
          .setProgressDate(model.getProgressDate())
          .build();
    }
    return event;
  }

  @Override
  public List<Event> toEntity(Collection<DbEvent> modelCollection) {

    final List<Event> eventList = new ArrayList<>(20);
    for (DbEvent dbEvent : modelCollection) {
      final Event event = toEntity(dbEvent);
      if (event != null) {
        eventList.add(event);
      }
    }
    return eventList;
  }


  @Override
  public DbEvent fromEntity(Event model, boolean copyId) {

    DbEvent dbEvent = null;
    if (model != null) {
      dbEvent = new DbEvent(model.getId());
      dbEvent.setName(model.getName());
      dbEvent.setDescription(model.getDescription());
      dbEvent.setDate(model.getDate());
      dbEvent.setFavorite(model.getFavorite());
      dbEvent.setTags(model.getTags());
      dbEvent.setReminder(model.getReminder());
      dbEvent.setReminderUnit(model.getReminderUnit());
      dbEvent.setTimeLapse(model.getTimeLapse());
      dbEvent.setTimeLapseUnit(model.getTimeLapseUnit());
      dbEvent.setProgressDate(model.getProgressDate());
    }
    return dbEvent;
  }

  @Override
  public List<DbEvent> fromEntity(Collection<Event> modelCollection) {

    final List<DbEvent> eventList = new ArrayList<>(20);
    for (Event event : modelCollection) {
      final DbEvent dbEvent = fromEntity(event, true);
      if (dbEvent != null) {
        eventList.add(dbEvent);
      }
    }
    return eventList;
  }
}
