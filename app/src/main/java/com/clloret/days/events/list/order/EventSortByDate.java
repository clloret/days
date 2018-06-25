package com.clloret.days.events.list.order;

import com.clloret.days.model.entities.Event;

public class EventSortByDate extends EventSortStrategy {

  EventSortByDate(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(Event event1, Event event2) {

    Event e1 = getFirstEvent(event1, event2);
    Event e2 = getSecondEvent(event1, event2);

    return e1.getDate().compareTo(e2.getDate());
  }
}
