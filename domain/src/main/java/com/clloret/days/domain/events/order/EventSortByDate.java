package com.clloret.days.domain.events.order;

public class EventSortByDate extends EventSortStrategy {

  EventSortByDate(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(EventSortable event1, EventSortable event2) {

    EventSortable e1 = getFirstEvent(event1, event2);
    EventSortable e2 = getSecondEvent(event1, event2);

    return e1.getDate().compareTo(e2.getDate());
  }
}
