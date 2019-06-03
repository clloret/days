package com.clloret.days.data.local.entities.converters;

import androidx.room.TypeConverter;
import com.clloret.days.domain.entities.Event.TimeUnit;

public class TimeUnitConverter {

  @TypeConverter
  public static TimeUnit toTimeUnit(String text) {

    if (text == null) {
      return null;
    }

    try {
      return TimeUnit.valueOf(text.toUpperCase());
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  @TypeConverter
  public static String toString(TimeUnit timeUnit) {

    return timeUnit == null ? null : timeUnit.toString();
  }
}
