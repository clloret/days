package com.clloret.days.events.list.order;

import com.clloret.days.events.list.order.EventSortStrategy.OrderType;
import com.clloret.days.model.entities.Event;
import java.util.Comparator;

public class EventSortFactory {

  public enum SortType {
    NAME(0),
    FAVORITE(1),
    LATEST_DATE(2),
    OLDEST_DATE(3);

    private final int value;

    SortType(final int value) {

      this.value = value;
    }

    public static SortType fromValue(int value) {

      for (SortType type : SortType.values()) {
        if (type.getValue() == value) {
          return type;
        }
      }
      return null;
    }

    public int getValue() {

      return value;
    }
  }

  public static Comparator<Event> makeEventSort(SortType type) {

    switch (type) {
      case NAME:
        return new EventSortByName(OrderType.ASC);
      case FAVORITE:
        return new EventSortByFavorite(OrderType.DESC);
      case LATEST_DATE:
        return new EventSortByDate(OrderType.DESC);
      case OLDEST_DATE:
        return new EventSortByDate(OrderType.ASC);
      default:
        throw new IllegalArgumentException("SortType not supported.");
    }
  }
}
