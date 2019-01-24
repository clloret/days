package com.clloret.days.domain.events.order;

import java.util.Date;

public interface EventSortable {

  boolean isFavorite();

  String getName();

  Date getDate();
}
