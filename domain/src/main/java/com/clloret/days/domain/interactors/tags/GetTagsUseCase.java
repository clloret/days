package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.types.SingleUseCaseWithParameter;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class GetTagsUseCase implements SingleUseCaseWithParameter<Boolean, List<Tag>> {

  private final AppDataStore dataStore;

  @Inject
  public GetTagsUseCase(AppDataStore dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Single<List<Tag>> execute(Boolean refresh) {

    return dataStore.getTags(refresh);
  }
}
