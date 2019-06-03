package com.clloret.days.data.local.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.clloret.days.data.local.entities.converters.TimeUnitConverter;
import com.clloret.days.domain.entities.Event.TimeUnit;
import java.util.Date;

@Entity(tableName = "events")
public class DbEvent {

  private static final String[] EMPTY_ARRAY = new String[0];

  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  private String id;

  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "description")
  private String description;

  @ColumnInfo(name = "date")
  private Date date;

  @ColumnInfo(name = "tag_id")
  private String[] tags = EMPTY_ARRAY;

  @ColumnInfo(name = "favorite")
  private boolean favorite;

  @ColumnInfo(name = "reminder")
  @Nullable
  private Integer reminder;

  @ColumnInfo(name = "reminder_unit")
  @TypeConverters(TimeUnitConverter.class)
  private TimeUnit reminderUnit;

  @ColumnInfo(name = "time_lapse")
  private int timeLapse;

  @ColumnInfo(name = "time_lapse_unit")
  @TypeConverters(TimeUnitConverter.class)
  private TimeUnit timeLapseUnit;

  public DbEvent(@NonNull String id, String name, String description, Date date, boolean favorite,
      @Nullable Integer reminder, TimeUnit reminderUnit, int timeLapse, TimeUnit timeLapseUnit) {

    this.id = id;
    this.name = name;
    this.description = description;
    this.date = date;
    this.favorite = favorite;
    this.reminder = reminder;
    this.reminderUnit = reminderUnit;
    this.timeLapse = timeLapse;
    this.timeLapseUnit = timeLapseUnit;
  }

  @Ignore
  public DbEvent(@NonNull String id) {

    this.id = id;
  }

  @NonNull
  public String getId() {

    return id;
  }

  public void setId(@NonNull String id) {

    this.id = id;
  }

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public Date getDate() {

    return (Date) date.clone();
  }

  public void setDate(Date date) {

    this.date = (Date) date.clone();
  }

  public boolean isFavorite() {

    return favorite;
  }

  public void setFavorite(boolean favorite) {

    this.favorite = favorite;
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

  public TimeUnit getReminderUnit() {

    return reminderUnit;
  }

  public void setReminderUnit(@NonNull TimeUnit reminderUnit) {

    this.reminderUnit = reminderUnit;
  }

  public int getTimeLapse() {

    return timeLapse;
  }

  public void setTimeLapse(int timeLapse) {

    this.timeLapse = timeLapse;
  }

  public TimeUnit getTimeLapseUnit() {

    return timeLapseUnit;
  }

  public void setTimeLapseUnit(TimeUnit timeLapseUnit) {

    this.timeLapseUnit = timeLapseUnit;
  }
}
