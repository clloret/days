package com.clloret.days.domain.events.filter;

import static com.google.common.truth.Truth.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import org.junit.Test;

public class EventFilterByFavoriteTest {

  @Test
  public void eventMatchFilter_WhenMatch_ReturnTrue() {

    EventFilterByFavorite sut = new EventFilterByFavorite();
    Event event = new EventBuilder().setFavorite(true).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isTrue();
  }

  @Test
  public void eventMatchFilter_WhenNotMatch_ReturnFalse() {

    EventFilterByFavorite sut = new EventFilterByFavorite();
    Event event = new EventBuilder().setFavorite(false).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isFalse();
  }

}