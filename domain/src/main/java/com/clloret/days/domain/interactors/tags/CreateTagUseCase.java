package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.repository.TagRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class CreateTagUseCase extends BaseMaybeUseCase<Tag, Tag> {

  private final TagRepository dataStore;

  @Inject
  public CreateTagUseCase(
      ThreadSchedulers threadSchedulers, TagRepository dataStore) {

    super(threadSchedulers);

    this.dataStore = dataStore;
  }

  @Override
  protected Maybe<Tag> buildUseCaseObservable(Tag tag) {

    return dataStore.create(tag);
  }
}
