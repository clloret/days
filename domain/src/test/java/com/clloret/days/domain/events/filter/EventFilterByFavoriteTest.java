package com.clloret.days.domain.events.filter;

import static org.junit.Assert.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import org.hamcrest.Matchers;
import org.junit.Test;

public class EventFilterByFavoriteTest {

  @Test
  public void eventMatchFilter_WhenMatch_ReturnTrue() {

    EventFilterByFavorite sut = new EventFilterByFavorite();
    Event event = new EventBuilder().setFavorite(true).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result, Matchers.is(true));
  }

  @Test
  public void eventMatchFilter_WhenNotMatch_ReturnFalse() {

    EventFilterByFavorite sut = new EventFilterByFavorite();
    Event event = new EventBuilder().setFavorite(false).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result, Matchers.is(false));
  }

}