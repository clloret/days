package com.clloret.days.data.local.entities.converters;

import android.text.TextUtils;
import androidx.room.TypeConverter;
import com.clloret.days.domain.entities.Event;

public class StringArrayConverter {

  @TypeConverter
  public static String[] toStringArray(String string) {

    return TextUtils.isEmpty(string) ? Event.Companion.getEMPTY_ARRAY() : string.split(",");
  }

  @TypeConverter
  public static String toString(String[] array) {

    return array == null ? null : TextUtils.join(",", array);
  }
}
