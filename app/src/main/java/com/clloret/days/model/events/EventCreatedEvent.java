package com.clloret.days.model.events;

import com.clloret.days.model.entities.Event;

public class EventCreatedEvent {

  public final Event event;

  public EventCreatedEvent(Event event) {

    this.event = event;
  }
}
