package com.clloret.days.domain.events.order;

public class EventSortByName extends EventSortStrategy {

  EventSortByName(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(EventSortable event1, EventSortable event2) {

    EventSortable e1 = getFirstEvent(event1, event2);
    EventSortable e2 = getSecondEvent(event1, event2);

    String name1 = e1.getName() != null ? e1.getName() : "";
    String name2 = e2.getName() != null ? e2.getName() : "";

    return name1.compareTo(name2);
  }

}
