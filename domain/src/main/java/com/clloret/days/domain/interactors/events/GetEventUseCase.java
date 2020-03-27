package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.base.BaseSingleUseCase;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetEventUseCase extends BaseSingleUseCase<String, Event> {

  private final EventRepository dataStore;

  @Inject
  public GetEventUseCase(ThreadSchedulers threadSchedulers, EventRepository dataStore) {

    super(threadSchedulers);

    this.dataStore = dataStore;
  }

  @Override
  protected Single<Event> buildUseCaseObservable(String parameter) {

    return dataStore.getById(parameter);
  }
}
