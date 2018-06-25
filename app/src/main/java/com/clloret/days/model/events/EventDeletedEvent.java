package com.clloret.days.model.events;

import com.clloret.days.model.entities.Event;

public class EventDeletedEvent {

  public final Event event;

  public EventDeletedEvent(Event event) {

    this.event = event;
  }
}
