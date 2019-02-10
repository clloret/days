package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.types.SingleUseCaseWithParameter;
import io.reactivex.Single;
import java.util.List;

public class GetEventsUseCase implements SingleUseCaseWithParameter<Boolean, List<Event>> {

  private final AppDataStore dataStore;

  public GetEventsUseCase(AppDataStore dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Single<List<Event>> execute(Boolean parameter) {

    return dataStore.getEvents(parameter);
  }
}
