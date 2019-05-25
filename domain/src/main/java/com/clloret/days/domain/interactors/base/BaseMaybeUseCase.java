package com.clloret.days.domain.interactors.base;

import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Maybe;

public abstract class BaseMaybeUseCase<P, R> extends BaseUseCase<Maybe<R>, P> {

  protected BaseMaybeUseCase(ThreadSchedulers threadSchedulers) {

    super(threadSchedulers);
  }

  public Maybe<R> execute(P parameter) {

    return buildUseCaseObservable(parameter)
        .subscribeOn(executorScheduler)
        .observeOn(uiScheduler);
  }
}