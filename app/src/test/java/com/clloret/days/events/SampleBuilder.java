package com.clloret.days.events;

import android.support.annotation.NonNull;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.model.entities.EventViewModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDate;

public class SampleBuilder {

  public static String emptyText = "";
  public static String id = "1";
  public static String name = "Mock Event";
  public static String description = "Description";
  public static Date date = new LocalDate(2000, 1, 1).toDate();
  public static boolean favorite = false;

  @NonNull
  public static Event createEvent() {

    return new EventBuilder()
        .setId(id)
        .setName(name)
        .setDescription(description)
        .setDate(date)
        .setFavorite(favorite)
        .build();
  }

  @NonNull
  public static EventViewModel createEventViewModel() {

    return new EventViewModel(id, name, description, date, favorite);
  }

  public static List<Event> createEventList() {

    List<Event> eventList = new ArrayList<>(5);

    for (int i = 0; i < 5; i++) {
      eventList.add(new Event());
    }

    return eventList;
  }

}
