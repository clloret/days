package com.clloret.days.domain.events.order;

public class EventSortByFavorite extends EventSortStrategy {

  EventSortByFavorite(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(EventSortable event1, EventSortable event2) {

    EventSortable e1 = getFirstEvent(event1, event2);
    EventSortable e2 = getSecondEvent(event1, event2);

    return Boolean.valueOf(e1.isFavorite()).compareTo(e2.isFavorite());
  }
}
