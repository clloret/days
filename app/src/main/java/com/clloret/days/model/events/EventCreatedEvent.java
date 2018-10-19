package com.clloret.days.model.events;

import com.clloret.days.model.entities.EventViewModel;

public class EventCreatedEvent {

  public final EventViewModel event;

  public EventCreatedEvent(EventViewModel event) {

    this.event = event;
  }
}
