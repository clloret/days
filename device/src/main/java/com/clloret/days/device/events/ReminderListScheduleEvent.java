package com.clloret.days.device.events;

import com.clloret.days.domain.entities.Event;
import java.util.List;

public class ReminderListScheduleEvent {

  public final List<Event> events;
  public final boolean add;

  public ReminderListScheduleEvent(List<Event> events, boolean add) {

    this.events = events;
    this.add = add;
  }

}
