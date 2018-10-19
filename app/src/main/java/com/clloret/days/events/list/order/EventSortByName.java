package com.clloret.days.events.list.order;

import com.clloret.days.model.entities.EventViewModel;

public class EventSortByName extends EventSortStrategy {

  EventSortByName(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(EventViewModel event1, EventViewModel event2) {

    EventViewModel e1 = getFirstEvent(event1, event2);
    EventViewModel e2 = getSecondEvent(event1, event2);

    return e1.getName().compareTo(e2.getName());
  }

}
