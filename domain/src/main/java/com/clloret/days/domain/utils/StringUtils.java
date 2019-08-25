package com.clloret.days.domain.utils;

public class StringUtils {

  private StringUtils() {

  }

  public static boolean isNullOrEmpty(String s) {

    if (s == null) {
      return true;
    }

    return s.isEmpty();
  }
}
