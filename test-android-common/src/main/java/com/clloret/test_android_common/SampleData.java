package com.clloret.test_android_common;

import android.content.Context;
import android.os.Build;
import androidx.test.platform.app.InstrumentationRegistry;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.DbTag;
import com.clloret.days.domain.entities.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class SampleData {

  private static <T> T read(String fileName, Type typeOfT) {

    final ClassLoader classLoader = Objects.requireNonNull(SampleData.class.getClassLoader());
    final InputStream is = classLoader.getResourceAsStream(fileName);
    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
    final Gson gson = new Gson();

    return gson.fromJson(bufferedReader, typeOfT);
  }

  private static Locale getCurrentLocale(Context context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      return context.getResources().getConfiguration().getLocales().get(0);
    } else {
      //noinspection deprecation
      return context.getResources().getConfiguration().locale;
    }
  }

  private static String getSamplesFileName(String name) {

    final Context context = InstrumentationRegistry.getInstrumentation()
        .getTargetContext();
    Locale locale = getCurrentLocale(context);
    final String languageTag = locale.toLanguageTag();

    return name + "_" + languageTag + ".json";
  }

  private static void createSampleTags(DaysDatabase db) {

    final String fileName = getSamplesFileName("tags");
    final List<DbTag> tags = read(fileName, new TypeToken<List<DbTag>>() {
    }.getType());

    db.tagDao().insertAll(tags);
  }

  private static void createSampleEvents(DaysDatabase db) {

    final String fileName = getSamplesFileName("events");
    final List<DbEvent> events = read(fileName, new TypeToken<List<DbEvent>>() {
    }.getType());

    db.eventDao().insertAll(events);
  }

  public static List<Event> getSampleEvents() {

    final String fileName = getSamplesFileName("events");

    return read(fileName, new TypeToken<List<Event>>() {
    }.getType());
  }

  public static void createEntities(DaysDatabase db) {

    createSampleTags(db);
    createSampleEvents(db);
  }

}
