package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Single;
import java.io.Serializable;
import java.util.List;

public abstract class EventFilterStrategy implements Serializable {

  public abstract Single<List<Event>> getEvents(EventRepository appDataStore);

  public boolean eventMatchFilter(Event event) {

    return false;
  }
}
