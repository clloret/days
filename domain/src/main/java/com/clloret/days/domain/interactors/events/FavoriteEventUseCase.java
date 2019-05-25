package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class FavoriteEventUseCase extends BaseMaybeUseCase<Event, Event> {

  private final EventRepository dataStore;

  @Inject
  public FavoriteEventUseCase(ThreadSchedulers threadSchedulers, EventRepository dataStore) {

    super(threadSchedulers);

    this.dataStore = dataStore;
  }

  @Override
  protected Maybe<Event> buildUseCaseObservable(Event event) {

    event.setFavorite(!event.isFavorite());

    return dataStore.edit(event);
  }
}
