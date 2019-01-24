package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import io.reactivex.Single;
import java.util.List;

public class EventFilterByPast extends EventFilterStrategy {

  @Override
  public Single<List<Event>> getEvents(AppDataStore appDataStore) {

    // TODO: 26/02/2018 Implement
    return null;
  }
}
