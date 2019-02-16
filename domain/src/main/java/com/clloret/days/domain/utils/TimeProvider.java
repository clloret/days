package com.clloret.days.domain.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public interface TimeProvider {

  LocalDate getCurrentDate();

  DateTime getCurrentTime();
}
