package com.clloret.days.model.events;

import com.clloret.days.model.entities.EventViewModel;

public class EventModifiedEvent {

  public final EventViewModel event;

  public EventModifiedEvent(EventViewModel event) {

    this.event = event;
  }
}
