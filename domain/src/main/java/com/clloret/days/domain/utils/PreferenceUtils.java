package com.clloret.days.domain.utils;

import com.clloret.days.domain.events.order.EventSortFactory.SortType;

public interface PreferenceUtils {

  boolean getUseRemoteDataStore();

  int getReminderTime();

  int getSortMode();

  void setSortMode(SortType sortType);

  int getViewColumnNumber();

  void setViewColumnNumber(int columns);

  String getAirtableApiKey();

  String getAirtableBaseId();

  String getDefaultList();

  boolean isAnalyticsEnabled();
}
