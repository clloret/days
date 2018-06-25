package com.clloret.days.events.list.filter;

import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import io.reactivex.Single;
import java.io.Serializable;
import java.util.List;

public abstract class EventFilterStrategy implements Serializable {

  public abstract Single<List<Event>> getEvents(AppDataStore appDataStore);
}
