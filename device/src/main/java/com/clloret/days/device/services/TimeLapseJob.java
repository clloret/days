package com.clloret.days.device.services;

import static org.joda.time.DateTimeConstants.MILLIS_PER_HOUR;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import java.util.List;
import java.util.Objects;
import timber.log.Timber;

public class TimeLapseJob {

  private static final int JOB_ID = 0;

  public static void scheduleJob(Context context) {

    if (isJobIdRunning(context, JOB_ID)) {
      Timber.d("Job is already scheduled");
      return;
    }

    ComponentName serviceComponent = new ComponentName(context, TimeLapseService.class);

    @SuppressLint("MissingPermission")
    JobInfo jobInfo = new Builder(JOB_ID, serviceComponent)
        .setPersisted(true)
        .setPeriodic(MILLIS_PER_HOUR)
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        .build();

    JobScheduler jobScheduler = Objects
        .requireNonNull(context.getSystemService(JobScheduler.class),
            "JobScheduler cannot be null");

    int result = jobScheduler.schedule(jobInfo);
    if (result == JobScheduler.RESULT_SUCCESS) {
      Timber.d("Successfully scheduled job: %s", result);
    } else {
      Timber.w("Job could not be scheduled: %s", result);
    }
  }

  private static boolean isJobIdRunning(Context context, int jobId) {

    final JobScheduler jobScheduler = (JobScheduler) context
        .getSystemService(Context.JOB_SCHEDULER_SERVICE);

    List<JobInfo> allPendingJobs = Objects.requireNonNull(jobScheduler).getAllPendingJobs();
    for (JobInfo jobInfo : allPendingJobs) {
      if (jobInfo.getId() == jobId) {
        return true;
      }
    }

    return false;
  }
}
