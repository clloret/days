package com.clloret.days.events;

import androidx.annotation.NonNull;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.model.entities.EventViewModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDate;

public class SampleBuilder {

  public static final String EMPTY_TEXT = "";
  private static final String ID = "1";
  private static final String NAME = "Mock Event";
  private static final String DESCRIPTION = "Description";
  private static final Date DATE = new LocalDate(2000, 1, 1).toDate();
  private static final boolean FAVORITE = false;
  private static final String[] EMPTY_ARRAY = new String[0];

  @NonNull
  public static Event createEvent() {

    return new Event(
        ID,
        NAME,
        DESCRIPTION,
        DATE,
        EMPTY_ARRAY,
        FAVORITE,
        null,
        TimeUnit.DAY,
        0,
        TimeUnit.DAY,
        null);
  }

  @NonNull
  public static EventViewModel createEventViewModel() {

    return new EventViewModel(
        ID,
        NAME,
        DESCRIPTION,
        DATE,
        EMPTY_ARRAY,
        FAVORITE,
        null,
        TimeUnit.DAY,
        0,
        TimeUnit.DAY,
        null);
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static List<Event> createEventList() {

    List<Event> eventList = new ArrayList<>(5);

    Event event1 = new EventBuilder()
        .setId("Id1")
        .setName("Sample Event 1")
        .setFavorite(true)
        .setDate(new Date())
        .build();
    eventList.add(event1);

    Event event2 = new EventBuilder()
        .setId("Id2")
        .setName("Filtered áéíóúàèìòù")
        .setFavorite(true)
        .setDate(new Date())
        .build();
    eventList.add(event2);

    return eventList;
  }

}
