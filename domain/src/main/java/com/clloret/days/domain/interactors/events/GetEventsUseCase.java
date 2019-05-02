package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.SingleUseCaseWithParameter;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class GetEventsUseCase implements SingleUseCaseWithParameter<Boolean, List<Event>> {

  private final EventRepository dataStore;

  @Inject
  public GetEventsUseCase(EventRepository dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Single<List<Event>> execute(Boolean parameter) {

    return dataStore.getAll(parameter);
  }
}
