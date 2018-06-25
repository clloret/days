package com.clloret.days.model.events;

import com.clloret.days.model.entities.Tag;

public class TagModifiedEvent {

  public final Tag tag;

  public TagModifiedEvent(Tag tag) {

    this.tag = tag;
  }
}
