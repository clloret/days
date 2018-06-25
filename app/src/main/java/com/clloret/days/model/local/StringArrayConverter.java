package com.clloret.days.model.local;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

public class StringArrayConverter {

  private static final String[] EMPTY_ARRAY = new String[0];

  @TypeConverter
  public static String[] toStringArray(String string) {

    return TextUtils.isEmpty(string) ? EMPTY_ARRAY : string.split(",");
  }

  @TypeConverter
  public static String toString(String[] array) {

    return array == null ? null : TextUtils.join(",", array);
  }
}
