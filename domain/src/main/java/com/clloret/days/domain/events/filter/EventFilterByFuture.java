package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Single;
import java.util.List;
import org.joda.time.LocalDate;

public class EventFilterByFuture extends EventFilterStrategy {

  private final LocalDate date;

  public EventFilterByFuture(LocalDate date) {

    this.date = date;
  }

  @Override
  public Single<List<Event>> getEvents(EventRepository appDataStore) {

    return appDataStore.getAfterDate(date);
  }

  @Override
  public boolean eventMatchFilter(Event event) {

    LocalDate localDate = new LocalDate(event.getDate());

    return localDate.isAfter(date);
  }

}
