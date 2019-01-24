package com.clloret.days.domain.events.filter;

import static org.junit.Assert.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import org.hamcrest.Matchers;
import org.junit.Test;

public class EventFilterByTagTest {

  @Test
  public void eventMatchFilter_WhenMatch_ReturnTrue() {

    EventFilterByTag sut = new EventFilterByTag("tag1");
    Event event = new EventBuilder().setTags(new String[]{"tag1", "tag2"}).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result, Matchers.is(true));
  }

  @Test
  public void eventMatchFilter_WhenMatchEmptyTags_ReturnTrue() {

    EventFilterByTag sut = new EventFilterByTag("");
    Event event = new EventBuilder().setTags(new String[]{}).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result, Matchers.is(true));
  }

  @Test
  public void eventMatchFilter_WhenNotMatch_ReturnFalse() {

    EventFilterByTag sut = new EventFilterByTag("tag1");
    Event event = new EventBuilder().setTags(new String[]{"tag2", "tag3"}).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result, Matchers.is(false));
  }
}