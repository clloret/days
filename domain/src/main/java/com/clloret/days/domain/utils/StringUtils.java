package com.clloret.days.domain.utils;

import java.text.Normalizer;

public class StringUtils {

  private StringUtils() {

  }

  public static boolean isNullOrEmpty(String s) {

    if (s == null) {
      return true;
    }

    return s.isEmpty();
  }

  public static String normalizeText(String text) {

    final String lowerCase = text.toLowerCase();
    return Normalizer
        .normalize(lowerCase, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", "");
  }

  public static String capitalizeFirstLetter(String text) {

    return text.substring(0, 1).toUpperCase() + text.substring(1);
  }

  public static Integer tryParseInt(String text) {

    try {
      return Integer.parseInt(text);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

}
