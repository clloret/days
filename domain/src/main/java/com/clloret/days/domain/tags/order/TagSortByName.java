package com.clloret.days.domain.tags.order;

public class TagSortByName extends TagSortStrategy {

  TagSortByName(OrderType orderType) {

    super(orderType);
  }

  @Override
  public int compare(TagSortable tag1, TagSortable tag2) {

    TagSortable e1 = getFirstEvent(tag1, tag2);
    TagSortable e2 = getSecondEvent(tag1, tag2);

    return e1.getName().compareTo(e2.getName());
  }

}
