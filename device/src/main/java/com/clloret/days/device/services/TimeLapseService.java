package com.clloret.days.device.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import com.clloret.days.device.eventbus.EventsUpdatedEvent;
import com.clloret.days.domain.timelapse.TimeLapseManager;
import dagger.android.AndroidInjection;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import timber.log.Timber;

public class TimeLapseService extends JobService {

  @Inject
  TimeLapseManager timeLapseManager;

  @Inject
  EventBus eventBus;

  private Disposable subscribe;

  @Override
  public void onCreate() {

    super.onCreate();

    AndroidInjection.inject(this);
  }

  @Override
  public boolean onStartJob(JobParameters params) {

    Timber.d("onStartJob");

    subscribe = timeLapseManager
        .updateEventsDatesFromTimeLapse()
        .doFinally(() -> {
          Timber.d("Job successfully completed");

          eventBus.post(new EventsUpdatedEvent());

          jobFinished(params, false);
        })
        .doOnError(throwable -> {
          Timber.e(throwable, "Job error");

          jobFinished(params, true);
        })
        .subscribeOn(Schedulers.io())
        .subscribe();

    return true;
  }

  @Override
  public boolean onStopJob(JobParameters params) {

    if (subscribe != null) {
      subscribe.dispose();
    }

    return true;
  }
}
