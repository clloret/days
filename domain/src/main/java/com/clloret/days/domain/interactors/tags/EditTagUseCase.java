package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.repository.TagRepository;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class EditTagUseCase implements MaybeUseCaseWithParameter<Tag, Tag> {

  private final TagRepository dataStore;

  @Inject
  public EditTagUseCase(TagRepository dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Maybe<Tag> execute(Tag tag) {

    return dataStore.edit(tag);
  }
}
