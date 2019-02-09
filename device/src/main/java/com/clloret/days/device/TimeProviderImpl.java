package com.clloret.days.device;

import com.clloret.days.domain.utils.TimeProvider;
import org.joda.time.LocalDate;

public class TimeProviderImpl implements TimeProvider {

  public LocalDate getCurrentDate() {

    return LocalDate.now();
  }
}
