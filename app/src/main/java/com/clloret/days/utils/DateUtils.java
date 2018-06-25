package com.clloret.days.utils;

import java.util.Date;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

  public static String formatDate(Date date) {

    LocalDate ld = new LocalDate(date);
    return formatDate(ld);
  }

  public static String formatDate(LocalDate date) {

    DateTimeFormatter fmt = DateTimeFormat.mediumDate();
    return fmt.print(date);
  }

}
