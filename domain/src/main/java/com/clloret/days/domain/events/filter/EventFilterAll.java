package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Single;
import java.util.List;

public class EventFilterAll extends EventFilterStrategy {

  @Override
  public Single<List<Event>> getEvents(EventRepository appDataStore) {

    return appDataStore.getAll(false);
  }

  @Override
  public boolean eventMatchFilter(Event event) {

    return true;
  }
}
