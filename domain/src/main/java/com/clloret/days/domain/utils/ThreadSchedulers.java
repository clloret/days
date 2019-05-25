package com.clloret.days.domain.utils;

import io.reactivex.Scheduler;

public interface ThreadSchedulers {

  Scheduler getExecutorScheduler();

  Scheduler getUiScheduler();
}
