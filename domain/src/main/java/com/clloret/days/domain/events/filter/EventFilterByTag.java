package com.clloret.days.domain.events.filter;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import io.reactivex.Single;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class EventFilterByTag extends EventFilterStrategy {

  private String tagId;

  public EventFilterByTag(@NotNull String tagId) {

    this.tagId = tagId;
  }

  @Override
  public Single<List<Event>> getEvents(AppDataStore appDataStore) {

    return appDataStore.getEventsByTagId(tagId);
  }

  @Override
  public boolean eventMatchFilter(Event event) {

    List<String> tags = Arrays.asList(event.getTags());

    if (tagId.isEmpty() && tags.isEmpty()) {
      return true;
    }

    return tags.contains(tagId);
  }
}
