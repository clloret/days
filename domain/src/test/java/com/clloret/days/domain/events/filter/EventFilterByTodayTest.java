package com.clloret.days.domain.events.filter;

import static com.google.common.truth.Truth.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import org.junit.Test;

public class EventFilterByTodayTest {

  @Test
  public void eventMatchFilter_WhenMatch_ReturnTrue() {

    EventFilterByToday sut = new EventFilterByToday(CommonDates.TEST_DATE_AFTER);
    Event event = new EventBuilder().setDate(CommonDates.TEST_DATE_AFTER.toDate()).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isTrue();
  }

  @Test
  public void eventMatchFilter_WhenNotMatch_ReturnFalse() {

    EventFilterByToday sut = new EventFilterByToday(CommonDates.TEST_DATE_BEFORE);
    Event event = new EventBuilder().setDate(CommonDates.TEST_DATE_AFTER.toDate()).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isFalse();
  }

}