package com.clloret.days.device;

import com.clloret.days.domain.utils.TimeProvider;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@Singleton
public class TimeProviderImpl implements TimeProvider {

  @Inject
  public TimeProviderImpl() {

  }

  @Override
  public LocalDate getCurrentDate() {

    return LocalDate.now();
  }

  @Override
  public DateTime getCurrentTime() {

    return DateTime.now();
  }
}
