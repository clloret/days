package com.clloret.days.device;

import org.joda.time.LocalDate;

public class TimeProvider {

  public LocalDate getCurrentDate() {

    return LocalDate.now();
  }
}
