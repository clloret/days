package com.clloret.days.domain.events;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.TimeProvider;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventPeriodFormatTest {

  private LocalDate yesterday;
  private LocalDate today;
  private LocalDate tomorrow;
  private LocalDate thousandDaysLater;
  private DateTime now;

  @Mock
  private TimeProvider timeProvider;

  @Mock
  private StringResourceProvider stringResourceProvider;

  @InjectMocks
  private EventPeriodFormat sut;

  private void addReminderManagerStubs() {

    when(timeProvider.getCurrentDate())
        .thenReturn(today);

    when(timeProvider.getCurrentTime())
        .thenReturn(now);

    when(stringResourceProvider.getPeriodFormatToday())
        .thenReturn("Today");

    when(stringResourceProvider.getPeriodFormatBefore())
        .thenReturn("%s ago");

    when(stringResourceProvider.getPeriodFormatAfter())
        .thenReturn("Within %s");
  }

  @Before
  public void setUp() {

    Locale.setDefault(Locale.ENGLISH);

    yesterday = new LocalDate(2000, 1, 1);
    today = new LocalDate(2000, 1, 2);
    tomorrow = new LocalDate(2000, 1, 3);
    thousandDaysLater = today.plusDays(1000);
    now = new DateTime()
        .withDate(today)
        .withTime(0, 0, 0, 0);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getTimeLapseFormatted_whenDateIsAfter_ReturnCorrectText() {

    addReminderManagerStubs();

    String result = sut.getTimeLapseFormatted(tomorrow.toDate(), today.toDate());
    String expected = "Within 1 day";

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void getTimeLapseFormatted_whenDateIsBefore_ReturnCorrectText() {

    addReminderManagerStubs();

    String result = sut.getTimeLapseFormatted(yesterday.toDate(), today.toDate());
    String expected = "1 day ago";

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void getTimeLapseFormatted__whenDateIsToday_ReturnCorrectText() {

    addReminderManagerStubs();

    String result = sut.getTimeLapseFormatted(today.toDate(), today.toDate());
    String expected = "Today";

    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void getDaysSinceFormatted() {

    addReminderManagerStubs();

    String result = sut.getDaysSinceFormatted(thousandDaysLater.toDate());
    String expected = "1,000";

    assertThat(result).isEqualTo(expected);
  }

}