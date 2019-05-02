package com.clloret.days.fakes;

import com.clloret.days.domain.utils.PreferenceUtils;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.LocalTime;
import timber.log.Timber;

@Singleton
public class TestPreferenceUtils implements PreferenceUtils {

  @Inject
  public TestPreferenceUtils() {

  }

  @Override
  public int getReminderTime() {

    LocalTime now = LocalTime.now();
    int result = (now.getHourOfDay() * 60) + now.getMinuteOfHour() + 1;

    Timber.d("getReminderTime: %d", result);

    return result;
  }
}
