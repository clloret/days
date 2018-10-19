package com.clloret.days.model.events;

import com.clloret.days.model.entities.TagViewModel;

public class TagCreatedEvent {

  public final TagViewModel tag;

  public TagCreatedEvent(TagViewModel tag) {

    this.tag = tag;
  }
}
