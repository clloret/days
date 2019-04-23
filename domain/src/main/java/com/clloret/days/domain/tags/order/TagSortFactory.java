package com.clloret.days.domain.tags.order;

import com.clloret.days.domain.tags.order.TagSortStrategy.OrderType;
import java.util.Comparator;

public class TagSortFactory {

  public enum SortType {
    NAME(0);

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

  public static Comparator<TagSortable> makeTagSort(SortType type) {

    //noinspection SwitchStatementWithTooFewBranches
    switch (type) {
      case NAME:
        return new TagSortByName(OrderType.ASC);
      default:
        throw new IllegalArgumentException("SortType not supported.");
    }
  }
}
