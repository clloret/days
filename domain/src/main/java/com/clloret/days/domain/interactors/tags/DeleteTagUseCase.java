package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class DeleteTagUseCase implements MaybeUseCaseWithParameter<Tag, Boolean> {

  private final AppDataStore dataStore;

  @Inject
  public DeleteTagUseCase(AppDataStore dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Maybe<Boolean> execute(Tag tag) {

    return dataStore.deleteTag(tag);
  }
}
