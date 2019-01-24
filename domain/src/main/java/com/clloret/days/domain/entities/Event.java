package com.clloret.days.domain.entities;

import com.clloret.days.domain.events.order.EventSortable;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public final class Event implements EventSortable {

  public static final int REMINDER_EVENT_DAY = 0;

  private static final String[] EMPTY_ARRAY = new String[0];
  private String id;
  private String name;
  private String description;
  private Date date;
  private String[] tags = EMPTY_ARRAY;
  private boolean favorite;
  private Integer reminder;
  private TimeUnit reminderUnit;
  private int timeLapse;
  private TimeUnit timeLapseUnit;

  public Event() {

  }

  public Event(String id, String name, String description, Date date, String[] tags,
      boolean favorite, Integer reminder, TimeUnit reminderUnit, int timeLapse,
      TimeUnit timeLapseUnit) {

    this.id = id;
    this.name = name;
    this.description = description;
    this.date = date;
    this.tags = tags;
    this.favorite = favorite;
    this.reminder = reminder;
    this.reminderUnit = reminderUnit;
    this.timeLapse = timeLapse;
    this.timeLapseUnit = timeLapseUnit;
  }

  public boolean hasReminder() {

    return reminder != null;
  }

  public String getId() {

    return id;
  }

  public void setId(String id) {

    this.id = id;
  }

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public Date getDate() {

    return date == null ? null : (Date) date.clone();
  }

  public void setDate(Date date) {

    this.date = date == null ? null : (Date) date.clone();
  }

  public boolean isFavorite() {

    return favorite;
  }

  public void setFavorite(boolean favorite) {

    this.favorite = favorite;
  }

  @Override
  public int hashCode() {

    return id != null ? id.hashCode() : 0;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Event event = (Event) o;

    return id != null ? id.equals(event.id) : event.id == null;
  }

  public String[] getTags() {

    return tags;
  }

  public void setTags(String[] tags) {

    this.tags = tags;
  }

  public String getDescription() {

    return description;
  }

  public void setDescription(String description) {

    this.description = description;
  }

  public Integer getReminder() {

    return reminder;
  }

  public void setReminder(Integer reminder) {

    this.reminder = reminder;
  }

  @NotNull
  public TimeUnit getReminderUnit() {

    return reminderUnit != null ? reminderUnit : TimeUnit.DAY;
  }

  public void setReminderUnit(TimeUnit reminderUnit) {

    this.reminderUnit = reminderUnit;
  }

  public int getTimeLapse() {

    return timeLapse;
  }

  public void setTimeLapse(int timeLapse) {

    this.timeLapse = timeLapse;
  }

  @NotNull
  public TimeUnit getTimeLapseUnit() {

    return timeLapseUnit != null ? timeLapseUnit : TimeUnit.DAY;
  }

  public void setTimeLapseUnit(TimeUnit timeLapseUnit) {

    this.timeLapseUnit = timeLapseUnit;
  }

  public enum TimeUnit {
    DAY("Day"),
    MONTH("Month"),
    YEAR("Year");

    private String text;

    TimeUnit(final String text) {

      this.text = text;
    }

    @Override
    public String toString() {

      return text;
    }
  }
}
