package com.clloret.days.domain.utils;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class RxImmediateThreadingSchedulers implements ThreadSchedulers {

  @Override
  public Scheduler getExecutorScheduler() {

    return Schedulers.trampoline();
  }

  @Override
  public Scheduler getUiScheduler() {

    return Schedulers.trampoline();
  }
}
