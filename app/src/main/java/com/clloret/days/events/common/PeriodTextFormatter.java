package com.clloret.days.events.common;

import static java.lang.Math.abs;

import android.content.res.Resources;
import com.clloret.days.R;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.model.entities.EventViewModel;
import javax.inject.Inject;

public class PeriodTextFormatter {

  private final Resources resources;

  @Inject
  public PeriodTextFormatter(Resources resources) {

    this.resources = resources;
  }

  private String formatPeriod(Integer period, TimeUnit timeUnit, String emptyPeriod,
      String zeroPeriod, String format) {

    String formattedText;
    if (period == null) {
      formattedText = emptyPeriod;
    } else {
      if (period == 0) {
        formattedText = zeroPeriod;
      } else {
        String timeUnitText = getTimeUnitPluralString(timeUnit, period);
        formattedText = String
            .format(format, abs(period), timeUnitText,
                period <= 0 ? resources.getString(R.string.event_details_before)
                    : resources.getString(R.string.event_details_after));
      }
    }
    return formattedText;
  }

  private String getTimeUnitPluralString(TimeUnit timeUnit, int reminder) {

    int pluralId;

    switch (timeUnit) {
      case MONTH:
        pluralId = R.plurals.month;
        break;
      case YEAR:
        pluralId = R.plurals.year;
        break;
      case DAY:
      default:
        pluralId = R.plurals.day;
        break;
    }
    return resources.getQuantityString(pluralId, abs(reminder));
  }

  public String formatReminder(EventViewModel event) {

    return formatPeriod(event.getReminder(), event.getReminderUnit(),
        resources.getString(R.string.event_details_no_reminder),
        resources.getString(R.string.event_details_notify_the_day_of_the_event),
        resources.getString(R.string.event_details_notify_format_message));
  }

  public String formatTimeLapseReset(EventViewModel event) {

    return formatPeriod(event.getTimeLapse(), event.getTimeLapseUnit(),
        resources.getString(R.string.event_details_do_not_reset_automatically),
        resources.getString(R.string.event_details_do_not_reset_automatically),
        resources.getString(R.string.event_details_reset_format_message));
  }
}
