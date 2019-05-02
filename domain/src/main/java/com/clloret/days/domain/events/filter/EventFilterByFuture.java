package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Single;
import java.util.List;

public class EventFilterByFuture extends EventFilterStrategy {

  @Override
  public Single<List<Event>> getEvents(EventRepository appDataStore) {

    // TODO: 26/02/2018 Implement
    return null;
  }
}
