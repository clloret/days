package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.types.MaybeUseCaseWithParameter;
import com.clloret.days.domain.repository.TagRepository;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class DeleteTagUseCase implements MaybeUseCaseWithParameter<Tag, Boolean> {

  private final TagRepository dataStore;

  @Inject
  public DeleteTagUseCase(TagRepository dataStore) {

    this.dataStore = dataStore;
  }

  @Override
  public Maybe<Boolean> execute(Tag tag) {

    return dataStore.delete(tag);
  }
}
