package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.types.SingleUseCaseWithParameter;
import io.reactivex.Single;
import java.util.List;

public class GetTagsUseCase implements SingleUseCaseWithParameter<Boolean, List<Tag>> {

  private final AppDataStore dataStore;

  public GetTagsUseCase(AppDataStore dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Single<List<Tag>> execute(Boolean refresh) {

    return dataStore.getTags(refresh);
  }
}
