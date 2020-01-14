package com.clloret.days.domain.events.filter;

import static com.google.common.truth.Truth.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import org.junit.Test;

public class EventFilterByReminderTest {

  @Test
  public void eventMatchFilter_WhenMatch_ReturnTrue() {

    EventFilterByReminder sut = new EventFilterByReminder();
    Event event = new EventBuilder().setReminder(0).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isTrue();
  }

  @Test
  public void eventMatchFilter_WhenNotMatch_ReturnFalse() {

    EventFilterByFavorite sut = new EventFilterByFavorite();
    Event event = new EventBuilder().build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isFalse();
  }

}