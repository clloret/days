package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import io.reactivex.Maybe;

public class FavoriteEventUseCase implements MaybeUseCaseWithParameter<Event, Event> {

  private final AppDataStore dataStore;

  public FavoriteEventUseCase(AppDataStore dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Maybe<Event> execute(Event event) {

    event.setFavorite(!event.isFavorite());

    return dataStore.editEvent(event);
  }
}
