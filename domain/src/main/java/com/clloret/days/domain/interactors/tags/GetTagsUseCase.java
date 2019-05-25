package com.clloret.days.domain.interactors.tags;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.base.BaseSingleUseCase;
import com.clloret.days.domain.repository.TagRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class GetTagsUseCase extends BaseSingleUseCase<Boolean, List<Tag>> {

  private final TagRepository dataStore;

  @Inject
  public GetTagsUseCase(ThreadSchedulers threadSchedulers, TagRepository dataStore) {

    super(threadSchedulers);

    this.dataStore = dataStore;
  }

  @Override
  protected Single<List<Tag>> buildUseCaseObservable(Boolean refresh) {

    return dataStore.getAll(refresh);
  }
}
