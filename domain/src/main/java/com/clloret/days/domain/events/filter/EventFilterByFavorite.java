package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Single;
import java.util.List;

public class EventFilterByFavorite extends EventFilterStrategy {

  @Override
  public Single<List<Event>> getEvents(EventRepository appDataStore) {

    return appDataStore.getByFavorite();
  }

  @Override
  public boolean eventMatchFilter(Event event) {

    return event.isFavorite();
  }
}
