package com.clloret.days.device.events;

import com.clloret.days.domain.entities.Event;

public class ReminderScheduleEvent {

  public final Event event;
  public final boolean add;
  public final boolean removePreviously;

  public ReminderScheduleEvent(Event event, boolean add, boolean removePreviously) {

    this.event = event;
    this.add = add;
    this.removePreviously = removePreviously;
  }
}
