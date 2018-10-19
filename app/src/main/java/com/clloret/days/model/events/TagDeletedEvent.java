package com.clloret.days.model.events;

import com.clloret.days.model.entities.TagViewModel;

public class TagDeletedEvent {

  public final TagViewModel tag;

  public TagDeletedEvent(TagViewModel tag) {

    this.tag = tag;
  }
}
