package com.clloret.days.domain.events.order;

import static com.google.common.truth.Truth.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.events.order.EventSortStrategy.OrderType;
import org.junit.Test;

public class EventSortByNameTest {

  private Event event1 = new EventBuilder()
      .setName("A")
      .build();
  private Event event2 = new EventBuilder()
      .setName("Z")
      .build();

  private EventSortByName sut = new EventSortByName(OrderType.ASC);

  @Test
  public void compare_WhenFirstNameIsLower_ReturnLessThanZero() {

    int result = sut.compare(event1, event2);

    assertThat(result).isLessThan(0);
  }

  @Test
  public void compare_WhenFirstNameIsGreater_ReturnGreaterThanZero() {

    int result = sut.compare(event2, event1);

    assertThat(result).isGreaterThan(0);
  }

  @Test
  public void compare_WhenNamesAreEquals_ReturnZero() {

    int result = sut.compare(event1, event1);

    assertThat(result).isEqualTo(0);
  }

}