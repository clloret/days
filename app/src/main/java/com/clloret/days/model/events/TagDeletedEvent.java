package com.clloret.days.model.events;

import com.clloret.days.model.entities.Tag;

public class TagDeletedEvent {

  public final Tag tag;

  public TagDeletedEvent(Tag tag) {

    this.tag = tag;
  }
}
