package com.clloret.days.domain.utils;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {

  private static final Logger logger = LoggerFactory.getLogger(DateUtils.class.getSimpleName());

  private DateUtils() {

  }

  public static String formatDate(Date date) {

    LocalDate ld = new LocalDate(date);
    return formatDate(ld);
  }

  public static String formatDate(LocalDate date) {

    DateTimeFormatter fmt = DateTimeFormat.mediumDate();
    return fmt.print(date);
  }

  public static Date getDateFromString(String strDate, LocalDate defaultDate) {

    DateTime dateTime = defaultDate.toDateTimeAtStartOfDay();
    if (strDate != null) {
      DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
      try {
        dateTime = formatter.parseDateTime(strDate);
      } catch (Exception ex) {
        logger.warn("Cannot parse the string date", ex);
      }
    }
    return dateTime.toDate();
  }

}
