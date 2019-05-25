package com.clloret.days.domain.interactors.base;

import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Single;

public abstract class BaseSingleUseCase<P, R> extends BaseUseCase<Single<R>, P> {

  protected BaseSingleUseCase(ThreadSchedulers threadSchedulers) {

    super(threadSchedulers);
  }

  public Single<R> execute(P parameter) {

    return buildUseCaseObservable(parameter)
        .subscribeOn(executorScheduler)
        .observeOn(uiScheduler);
  }
}