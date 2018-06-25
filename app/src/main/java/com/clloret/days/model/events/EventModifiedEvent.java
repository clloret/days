package com.clloret.days.model.events;

import com.clloret.days.model.entities.Event;

public class EventModifiedEvent {

  public final Event event;

  public EventModifiedEvent(Event event) {

    this.event = event;
  }
}
