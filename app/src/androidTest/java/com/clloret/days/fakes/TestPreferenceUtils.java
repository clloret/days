package com.clloret.days.fakes;

import com.clloret.days.domain.events.order.EventSortFactory.SortType;
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
  public boolean getUseRemoteDataStore() {

    return false;
  }

  @Override
  public int getReminderTime() {

    LocalTime now = LocalTime.now();
    int result = (now.getHourOfDay() * 60) + now.getMinuteOfHour() + 1;

    Timber.d("getReminderTime: %d", result);

    return result;
  }

  @Override
  public int getSortMode() {

    return 0;
  }

  @Override
  public void setSortMode(SortType sortType) {

  }

  @Override
  public String getAirtableApiKey() {

    return null;
  }

  @Override
  public String getAirtableBaseId() {

    return null;
  }

  @Override
  public String getDefaultList() {

    return "0";
  }
}
