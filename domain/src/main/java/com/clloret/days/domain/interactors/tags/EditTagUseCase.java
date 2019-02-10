package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import io.reactivex.Maybe;

public class EditTagUseCase implements MaybeUseCaseWithParameter<Tag, Tag> {

  private final AppDataStore dataStore;

  public EditTagUseCase(AppDataStore dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Maybe<Tag> execute(Tag tag) {

    return dataStore.editTag(tag);
  }
}
