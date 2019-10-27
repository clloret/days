package com.clloret.days.domain.events.filter;

import static com.google.common.truth.Truth.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import org.junit.Test;

public class EventFilterByTagTest {

  @Test
  public void eventMatchFilter_WhenMatch_ReturnTrue() {

    EventFilterByTag sut = new EventFilterByTag("tag1");
    Event event = new EventBuilder().setTags(new String[]{"tag1", "tag2"}).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isTrue();
  }

  @Test
  public void eventMatchFilter_WhenMatchEmptyTags_ReturnTrue() {

    EventFilterByTag sut = new EventFilterByTag("");
    Event event = new EventBuilder().setTags(new String[]{}).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isTrue();
  }

  @Test
  public void eventMatchFilter_WhenNotMatch_ReturnFalse() {

    EventFilterByTag sut = new EventFilterByTag("tag1");
    Event event = new EventBuilder().setTags(new String[]{"tag2", "tag3"}).build();

    boolean result = sut.eventMatchFilter(event);

    assertThat(result).isFalse();
  }
}