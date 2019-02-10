package com.clloret.days.domain.interactors.types;

import io.reactivex.Single;

public interface SingleUseCaseWithParameter<P, R> {

  Single<R> execute(P parameter);
}
