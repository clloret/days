package com.clloret.days.device;

import com.clloret.days.domain.utils.TimeProvider;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class TimeProviderImpl implements TimeProvider {

  @Override
  public LocalDate getCurrentDate() {

    return LocalDate.now();
  }

  @Override
  public DateTime getCurrentTime() {

    return DateTime.now();
  }
}
