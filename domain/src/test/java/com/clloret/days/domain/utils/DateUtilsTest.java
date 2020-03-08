package com.clloret.days.domain.utils;

import static com.google.common.truth.Truth.assertThat;

import java.util.Date;
import java.util.Locale;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class DateUtilsTest {

  private LocalDate localDate = new LocalDate()
      .withDayOfMonth(15)
      .withMonthOfYear(2)
      .withYear(2018);

  @Before
  public void setUp() {

    Locale.setDefault(Locale.ENGLISH);
  }


  @Test
  public void formatDate_WhenUseDate_ReturnFormattedDate() {

    String result = DateUtils.formatDate(localDate.toDate());

    assertThat(result).isEqualTo("Feb 15, 2018");
  }

  @Test
  public void formatDate_WhenUseLocalDate_ReturnFormattedDate() {

    String result = DateUtils.formatDate(localDate);

    assertThat(result).isEqualTo("Feb 15, 2018");
  }

  @Test
  public void getDateFromString_Always_ReturnDate() {

    Date result = DateUtils.getDateFromString("15/02/2018", LocalDate.now());

    assertThat(result).isEqualTo(localDate.toDate());
  }

  @Test
  public void getDateFromString_WhenIncorrectDate_ReturnDefaultDate() {

    LocalDate defaultDate = new LocalDate()
        .withDayOfMonth(15)
        .withMonthOfYear(6)
        .withYear(2018);

    Date result = DateUtils.getDateFromString("15-02-2018", defaultDate);

    assertThat(result).isEqualTo(defaultDate.toDate());
  }

}