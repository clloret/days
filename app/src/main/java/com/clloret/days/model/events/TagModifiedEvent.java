package com.clloret.days.model.events;

import com.clloret.days.model.entities.TagViewModel;

public class TagModifiedEvent {

  public final TagViewModel tag;

  public TagModifiedEvent(TagViewModel tag) {

    this.tag = tag;
  }
}
