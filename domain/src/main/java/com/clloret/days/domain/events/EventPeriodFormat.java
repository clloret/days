package com.clloret.days.domain.events;

import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.TimeProvider;
import java.text.NumberFormat;
import java.util.Date;
import javax.inject.Inject;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventPeriodFormat {

  private final Logger logger = LoggerFactory.getLogger(EventPeriodFormat.class.getSimpleName());
  private final TimeProvider timeProvider;
  private final StringResourceProvider stringResourceProvider;

  @Inject
  public EventPeriodFormat(TimeProvider timeProvider,
      StringResourceProvider stringResourceProvider) {

    this.timeProvider = timeProvider;
    this.stringResourceProvider = stringResourceProvider;
  }

  private int getDaysSince(Date date) {

    final LocalDate startDate = timeProvider.getCurrentDate();
    final LocalDate endDate = new LocalDate(date);

    return Days.daysBetween(startDate, endDate).getDays();
  }

  public String getDaysSinceFormatted(Date date) {

    final int daysSince = getDaysSince(date);

    return NumberFormat.getInstance().format(daysSince);
  }

  private String getDaysSinceFormattedWithWords(Date fromDate, Date toDate, PeriodType periodType) {

    final LocalDate localFromDate = new LocalDate(toDate);
    final LocalDate localToDate = new LocalDate(fromDate);
    final Period period = new Period(
        localFromDate.isBefore(localToDate) ? localFromDate : localToDate,
        localToDate.isAfter(localFromDate) ? localToDate : localFromDate,
        periodType);
    final PeriodFormatter periodFormatter = PeriodFormat.wordBased();

    return periodFormatter.print(period);
  }

  public String getTimeLapseFormatted(Date fromDate, Date toDate, PeriodType periodType) {

    final LocalDate localFromDate = new LocalDate(fromDate);
    final LocalDate localToDate = new LocalDate(toDate);

    logger.debug("localFromDate: {} - localToDate: {}", localFromDate.toString(),
        localToDate.toString());

    if (localFromDate.equals(localToDate)) {

      return stringResourceProvider.getPeriodFormatToday();
    } else {

      String daysSinceFormattedWithWords = getDaysSinceFormattedWithWords(fromDate, toDate,
          periodType);
      String formatText =
          localFromDate.isBefore(localToDate) ? stringResourceProvider.getPeriodFormatBefore()
              : stringResourceProvider.getPeriodFormatAfter();

      return String.format(formatText, daysSinceFormattedWithWords);
    }
  }

}
