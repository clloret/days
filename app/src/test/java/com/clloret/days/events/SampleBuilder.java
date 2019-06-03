package com.clloret.days.events;

import androidx.annotation.NonNull;
import com.clloret.days.domain.entities.Event;
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

  @NonNull
  public static Event createEvent() {

    return new EventBuilder()
        .setId(ID)
        .setName(NAME)
        .setDescription(DESCRIPTION)
        .setDate(DATE)
        .setFavorite(FAVORITE)
        .build();
  }

  @NonNull
  public static EventViewModel createEventViewModel() {

    return new EventViewModel(ID, NAME, DESCRIPTION, DATE, FAVORITE);
  }

  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  public static List<Event> createEventList() {

    List<Event> eventList = new ArrayList<>(5);

    for (int i = 0; i < 5; i++) {
      eventList.add(new Event());
    }

    return eventList;
  }

}
