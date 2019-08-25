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

public class EventPeriodFormat {

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

  private String getDaysSinceFormattedWithWords(Date date) {

    final LocalDate startDate = timeProvider.getCurrentDate();
    final LocalDate endDate = new LocalDate(date);

    final Period period = new Period(startDate.isBefore(endDate) ? startDate : endDate,
        endDate.isAfter(startDate) ? endDate : startDate, PeriodType.days());
    final PeriodFormatter periodFormatter = PeriodFormat.wordBased();

    return periodFormatter.print(period);
  }

  public String getTimeLapseFormatted(Date date) {

    final LocalDate localDate = new LocalDate(date);
    final LocalDate currentDate = timeProvider.getCurrentDate();

    if ((localDate).equals(currentDate)) {

      return stringResourceProvider.getPeriodFormatToday();
    } else {

      String daysSinceFormattedWithWords = getDaysSinceFormattedWithWords(date);
      String formatText =
          localDate.isBefore(currentDate) ? stringResourceProvider.getPeriodFormatBefore()
              : stringResourceProvider.getPeriodFormatAfter();

      return String.format(formatText, daysSinceFormattedWithWords);
    }
  }

}
