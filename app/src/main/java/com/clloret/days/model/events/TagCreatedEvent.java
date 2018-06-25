package com.clloret.days.model.events;

import com.clloret.days.model.entities.Tag;

public class TagCreatedEvent {

  public final Tag tag;

  public TagCreatedEvent(Tag tag) {

    this.tag = tag;
  }
}
