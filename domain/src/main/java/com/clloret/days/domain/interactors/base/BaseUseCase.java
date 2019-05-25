package com.clloret.days.domain.interactors.base;

import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Scheduler;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public abstract class BaseUseCase<T, P> {

  protected final Scheduler executorScheduler;
  protected final Scheduler uiScheduler;

  protected BaseUseCase(@NotNull ThreadSchedulers threadSchedulers) {

    Objects.requireNonNull(threadSchedulers);

    executorScheduler = Objects.requireNonNull(threadSchedulers.getExecutorScheduler());
    uiScheduler = Objects.requireNonNull(threadSchedulers.getUiScheduler());
  }

  protected abstract T buildUseCaseObservable(P parameter);

}
