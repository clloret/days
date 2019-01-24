package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import io.reactivex.Single;
import java.util.List;

public class EventFilterAll extends EventFilterStrategy {

  @Override
  public Single<List<Event>> getEvents(AppDataStore appDataStore) {

    return appDataStore.getEvents(false);
  }

  @Override
  public boolean eventMatchFilter(Event event) {

    return true;
  }
}
