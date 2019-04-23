package com.clloret.days.domain.tags.order;

import java.io.Serializable;
import java.util.Comparator;

abstract class TagSortStrategy implements Comparator<TagSortable>, Serializable {

  private OrderType orderType;

  TagSortStrategy(OrderType orderType) {

    this.orderType = orderType;
  }

  TagSortable getFirstEvent(TagSortable tag1, TagSortable tag2) {

    return orderType == OrderType.ASC ? tag1 : tag2;
  }

  TagSortable getSecondEvent(TagSortable tag1, TagSortable tag2) {

    return orderType == OrderType.ASC ? tag2 : tag1;
  }

  public enum OrderType {
    DESC, ASC
  }

}
