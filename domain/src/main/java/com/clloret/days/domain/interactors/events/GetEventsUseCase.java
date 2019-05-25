package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.base.BaseSingleUseCase;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class GetEventsUseCase extends BaseSingleUseCase<Boolean, List<Event>> {

  private final EventRepository dataStore;

  @Inject
  public GetEventsUseCase(ThreadSchedulers threadSchedulers, EventRepository dataStore) {

    super(threadSchedulers);

    this.dataStore = dataStore;
  }

  @Override
  protected Single<List<Event>> buildUseCaseObservable(Boolean parameter) {

    return dataStore.getAll(parameter);
  }
}
