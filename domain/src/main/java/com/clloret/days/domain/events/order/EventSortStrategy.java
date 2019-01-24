package com.clloret.days.domain.events.order;

import java.io.Serializable;
import java.util.Comparator;

abstract class EventSortStrategy implements Comparator<EventSortable>, Serializable {

  private OrderType orderType;

  EventSortStrategy(OrderType orderType) {

    this.orderType = orderType;
  }

  EventSortable getFirstEvent(EventSortable event1, EventSortable event2) {

    return orderType == OrderType.ASC ? event1 : event2;
  }

  EventSortable getSecondEvent(EventSortable event1, EventSortable event2) {

    return orderType == OrderType.ASC ? event2 : event1;
  }

  public enum OrderType {
    DESC, ASC
  }

}
