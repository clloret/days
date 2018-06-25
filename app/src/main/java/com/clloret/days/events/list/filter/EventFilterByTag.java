package com.clloret.days.events.list.filter;

import android.support.annotation.NonNull;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import io.reactivex.Single;
import java.util.List;

public class EventFilterByTag extends EventFilterStrategy {

  private String tagId;

  public EventFilterByTag(@NonNull String tagId) {

    this.tagId = tagId;
  }

  @Override
  public Single<List<Event>> getEvents(AppDataStore appDataStore) {

    return appDataStore.getEventsByTagId(tagId);
  }
}
