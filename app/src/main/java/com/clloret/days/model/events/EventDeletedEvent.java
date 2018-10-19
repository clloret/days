package com.clloret.days.model.events;

import com.clloret.days.model.entities.EventViewModel;

public class EventDeletedEvent {

  public final EventViewModel event;

  public EventDeletedEvent(EventViewModel event) {

    this.event = event;
  }
}
