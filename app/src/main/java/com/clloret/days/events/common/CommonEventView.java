package com.clloret.days.events.common;

public interface CommonEventView {

  void showPeriodText(String text);

  void showDate(String text);

  void showSelectedTags(String text);

  void showSelectedReminder(String text);

  void showSelectedTimeLapseReset(String text);

  void showError(String text);
}
