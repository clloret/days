package com.clloret.days.domain.utils;

import static com.google.common.truth.Truth.assertThat;

import org.joda.time.LocalDate;
import org.junit.Test;

public class DateUtilsTest {

  private LocalDate localDate = new LocalDate()
      .withDayOfMonth(1)
      .withMonthOfYear(1)
      .withYear(2018);


  @Test
  public void formatDate_WhenUseDate_ReturnFormattedDate() {

    String result = DateUtils.formatDate(localDate.toDate());

    assertThat(result).isEqualTo("Jan 1, 2018");
  }

  @Test
  public void formatDate_WhenUseLocalDate_ReturnFormattedDate() {

    String result = DateUtils.formatDate(localDate);

    assertThat(result).isEqualTo("Jan 1, 2018");
  }
}