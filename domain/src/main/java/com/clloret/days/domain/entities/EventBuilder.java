package com.clloret.days.domain.entities;

import com.clloret.days.domain.entities.Event.TimeUnit;
import java.util.Date;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EventBuilder {

  private static final String[] EMPTY_ARRAY = new String[0];

  private String id;
  private String name = "";
  private String description;
  private Date date = new Date();
  private String[] tags = EMPTY_ARRAY;
  private boolean favorite;
  private Integer reminder;
  private TimeUnit reminderUnit = TimeUnit.DAY;
  private int timeLapse;
  private TimeUnit timeLapseUnit = TimeUnit.DAY;
  private Date progressDate;

  public EventBuilder setId(@Nullable String id) {

    this.id = id;
    return this;
  }

  public EventBuilder setName(@NotNull String name) {

    this.name = name;
    return this;
  }

  public EventBuilder setDescription(@Nullable String description) {

    this.description = description;
    return this;
  }

  public EventBuilder setDate(@Nullable Date date) {

    this.date = date;
    return this;
  }

  public EventBuilder setTags(@NotNull String[] tags) {

    this.tags = tags;
    return this;
  }

  public EventBuilder setFavorite(boolean favorite) {

    this.favorite = favorite;
    return this;
  }

  public EventBuilder setReminder(@Nullable Integer reminder) {

    this.reminder = reminder;
    return this;
  }

  public EventBuilder setReminderUnit(@NotNull TimeUnit reminderUnit) {

    this.reminderUnit = reminderUnit;
    return this;
  }

  public EventBuilder setTimeLapse(int timeLapse) {

    this.timeLapse = timeLapse;
    return this;
  }

  public EventBuilder setTimeLapseUnit(@NotNull TimeUnit timeLapseUnit) {

    this.timeLapseUnit = timeLapseUnit;
    return this;
  }

  public EventBuilder setProgressDate(@Nullable Date progressDate) {

    this.progressDate = progressDate;
    return this;
  }

  public Event build() {

    return new Event(
        id,
        name,
        description,
        date,
        tags,
        favorite,
        reminder,
        reminderUnit,
        timeLapse,
        timeLapseUnit,
        progressDate
    );
  }
}