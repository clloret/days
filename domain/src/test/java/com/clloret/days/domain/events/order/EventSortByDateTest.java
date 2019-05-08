package com.clloret.days.domain.events.order;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.events.order.EventSortStrategy.OrderType;
import java.util.Date;
import org.joda.time.LocalDate;
import org.junit.Test;

public class EventSortByDateTest {

  private Date oldestDate = new LocalDate()
      .withDayOfMonth(1)
      .withMonthOfYear(1)
      .withYear(2018)
      .toDate();

  private Date latestDate = new LocalDate()
      .withDayOfMonth(2)
      .withMonthOfYear(2)
      .withYear(2019)
      .toDate();

  private Event event1 = new EventBuilder()
      .setDate(oldestDate)
      .build();

  private Event event2 = new EventBuilder()
      .setDate(latestDate)
      .build();

  private EventSortByDate sut = new EventSortByDate(OrderType.ASC);

  @Test
  public void compare_WhenFirstNameIsLower_ReturnLessThanZero() {

    int result = sut.compare(event1, event2);

    assertThat(result, lessThan(0));
  }

  @Test
  public void compare_WhenFirstNameIsGreater_ReturnGreaterThanZero() {

    int result = sut.compare(event2, event1);

    assertThat(result, greaterThan(0));
  }

  @Test
  public void compare_whenNamesAreEquals_ReturnZero() {

    int result = sut.compare(event1, event1);

    assertThat(result, equalTo(0));
  }

}