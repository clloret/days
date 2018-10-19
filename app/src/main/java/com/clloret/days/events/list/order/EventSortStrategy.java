package com.clloret.days.events.list.order;

import com.clloret.days.model.entities.EventViewModel;
import java.io.Serializable;
import java.util.Comparator;

abstract class EventSortStrategy implements Comparator<EventViewModel>, Serializable {

  private OrderType orderType;

  EventSortStrategy(OrderType orderType) {

    this.orderType = orderType;
  }

  EventViewModel getFirstEvent(EventViewModel event1, EventViewModel event2) {

    return orderType == OrderType.ASC ? event1 : event2;
  }

  EventViewModel getSecondEvent(EventViewModel event1, EventViewModel event2) {

    return orderType == OrderType.ASC ? event2 : event1;
  }

  public enum OrderType {
    DESC, ASC
  }

}
