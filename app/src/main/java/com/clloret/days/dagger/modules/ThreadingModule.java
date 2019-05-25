package com.clloret.days.dagger.modules;

import com.clloret.days.domain.injection.TypeNamed;
import com.clloret.days.domain.utils.ThreadSchedulers;
import com.clloret.days.utils.ThreadSchedulersImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public abstract class ThreadingModule {

  @Provides
  @Singleton
  @Named(TypeNamed.EXECUTOR_SCHEDULER)
  static Scheduler provideExecutorThread() {

    return Schedulers.io();
  }

  @Provides
  @Singleton
  @Named(TypeNamed.UI_SCHEDULER)
  static Scheduler provideUiThread() {

    return AndroidSchedulers.mainThread();
  }

  @Binds
  abstract ThreadSchedulers bindThreadSchedulers(ThreadSchedulersImpl impl);

}
