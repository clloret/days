package com.clloret.days.events.list.order;

import com.clloret.days.model.entities.EventViewModel;

public class EventSortByFavorite extends EventSortStrategy {

  EventSortByFavorite(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(EventViewModel event1, EventViewModel event2) {

    EventViewModel e1 = getFirstEvent(event1, event2);
    EventViewModel e2 = getSecondEvent(event1, event2);

    return Boolean.valueOf(e1.isFavorite()).compareTo(e2.isFavorite());
  }
}
