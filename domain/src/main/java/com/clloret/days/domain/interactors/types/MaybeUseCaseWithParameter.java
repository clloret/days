package com.clloret.days.domain.interactors.types;

import io.reactivex.Maybe;

public interface MaybeUseCaseWithParameter<P, R> {

  Maybe<R> execute(P parameter);
}