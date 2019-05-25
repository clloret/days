package com.clloret.days.utils;

import com.clloret.days.domain.injection.TypeNamed;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.Scheduler;
import javax.inject.Inject;
import javax.inject.Named;

public class ThreadSchedulersImpl implements ThreadSchedulers {

  private final Scheduler executorScheduler;
  private final Scheduler uiScheduler;

  @Inject
  public ThreadSchedulersImpl(@Named(TypeNamed.EXECUTOR_SCHEDULER) Scheduler executorScheduler,
      @Named(TypeNamed.UI_SCHEDULER) Scheduler uiScheduler) {

    this.executorScheduler = executorScheduler;
    this.uiScheduler = uiScheduler;
  }


  @Override
  public Scheduler getExecutorScheduler() {

    return executorScheduler;
  }

  @Override
  public Scheduler getUiScheduler() {

    return uiScheduler;
  }
}
