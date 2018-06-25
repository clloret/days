package com.clloret.days.events.list.order;

import com.clloret.days.model.entities.Event;
import java.io.Serializable;
import java.util.Comparator;

abstract class EventSortStrategy implements Comparator<Event>, Serializable {

  private OrderType orderType;

  EventSortStrategy(OrderType orderType) {

    this.orderType = orderType;
  }

  Event getFirstEvent(Event event1, Event event2) {

    return orderType == OrderType.ASC ? event1 : event2;
  }

  Event getSecondEvent(Event event1, Event event2) {

    return orderType == OrderType.ASC ? event2 : event1;
  }

  public enum OrderType {
    DESC, ASC
  }

}
