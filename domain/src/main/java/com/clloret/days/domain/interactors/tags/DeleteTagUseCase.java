package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.base.BaseMaybeUseCase;
import com.clloret.days.domain.repository.TagRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;
import javax.inject.Inject;

public class DeleteTagUseCase extends BaseMaybeUseCase<Tag, Boolean> {

  private final TagRepository dataStore;

  @Inject
  public DeleteTagUseCase(ThreadSchedulers threadSchedulers, TagRepository dataStore) {

    super(threadSchedulers);

    this.dataStore = dataStore;
  }

  @Override
  protected Maybe<Boolean> buildUseCaseObservable(Tag tag) {

    return dataStore.delete(tag);
  }
}
