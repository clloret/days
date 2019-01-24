package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import io.reactivex.Single;
import java.util.List;

public class EventFilterByFavorite extends EventFilterStrategy {

  @Override
  public Single<List<Event>> getEvents(AppDataStore appDataStore) {

    return appDataStore.getEventsByFavorite();
  }

  @Override
  public boolean eventMatchFilter(Event event) {

    return event.isFavorite();
  }
}
