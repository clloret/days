package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class FavoriteEventUseCase implements MaybeUseCaseWithParameter<Event, Event> {

  private final EventRepository dataStore;

  @Inject
  public FavoriteEventUseCase(EventRepository dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Maybe<Event> execute(Event event) {

    event.setFavorite(!event.isFavorite());

    return dataStore.edit(event);
  }
}
