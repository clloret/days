package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Single;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDate;

public class EventFilterByPast extends EventFilterStrategy {

  private final LocalDate date;

  public EventFilterByPast(LocalDate date) {

    this.date = date;
  }

  @Override
  public Single<List<Event>> getEvents(EventRepository appDataStore) {

    return appDataStore.getBeforeDate(date);
  }

  @Override
  public boolean eventMatchFilter(Event event) {

    Date eventDate = event.getDate();

    if (eventDate == null) {
      return false;
    }

    LocalDate localDate = new LocalDate(event.getDate());

    return localDate.isBefore(date);
  }

}
