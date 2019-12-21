package com.clloret.days.domain.events.filter;

import org.joda.time.LocalDate;

class CommonDates {

  static final LocalDate TEST_DATE_AFTER = new LocalDate()
      .withDayOfMonth(1)
      .withMonthOfYear(6)
      .withYear(2019);

  static final LocalDate TEST_DATE_BEFORE = new LocalDate()
      .withDayOfMonth(31)
      .withMonthOfYear(5)
      .withYear(2019);
}
