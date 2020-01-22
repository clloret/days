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

}
