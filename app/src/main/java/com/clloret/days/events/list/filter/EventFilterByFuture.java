package com.clloret.days.events.list.filter;

import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import io.reactivex.Single;
import java.util.List;

public class EventFilterByFuture extends EventFilterStrategy {

  @Override
  public Single<List<Event>> getEvents(AppDataStore appDataStore) {

    // TODO: 26/02/2018 Implement
    return null;
  }
}
