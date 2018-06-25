package com.clloret.days.events.list.order;

import com.clloret.days.model.entities.Event;

public class EventSortByFavorite extends EventSortStrategy {

  EventSortByFavorite(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(Event event1, Event event2) {

    Event e1 = getFirstEvent(event1, event2);
    Event e2 = getSecondEvent(event1, event2);

    return Boolean.valueOf(e1.isFavorite()).compareTo(e2.isFavorite());
  }
}
