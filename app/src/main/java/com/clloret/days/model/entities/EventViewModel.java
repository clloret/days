package com.clloret.days.model.entities;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.events.order.EventSortable;
import java.util.Date;
import java.util.Objects;
import timber.log.Timber;

// If replace Parcelable, maintain field "event" implementation for Date type
public final class EventViewModel implements Parcelable, Cloneable, EventSortable {

  public static final Creator<EventViewModel> CREATOR = new Creator<EventViewModel>() {
    @Override
    public EventViewModel createFromParcel(Parcel in) {

      return new EventViewModel(in);
    }

    @Override
    public EventViewModel[] newArray(int size) {

      return new EventViewModel[size];
    }
  };

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

  // empty constructor needed by the Parceler library
  public EventViewModel() {

  }

  protected EventViewModel(Parcel in) {

    id = in.readString();
    name = in.readString();
    description = in.readString();
    long tmpDate = in.readLong();
    this.date = tmpDate == -1 ? null : new Date(tmpDate);
    tags = in.createStringArray();
    favorite = in.readByte() != 0;
    reminder = (Integer) in.readValue(Integer.class.getClassLoader());
    reminderUnit = (TimeUnit) in.readValue(TimeUnit.class.getClassLoader());
    timeLapse = in.readInt();
    timeLapseUnit = (TimeUnit) in.readValue(TimeUnit.class.getClassLoader());
  }

  public EventViewModel(String id, String name, String description, Date date, String[] tags,
      boolean favorite) {

    this.id = id;
    this.name = name;
    this.description = description;
    this.date = date;
    this.tags = tags.clone();
    this.favorite = favorite;
  }

  public EventViewModel(String id, String name, String description, Date date, boolean favorite) {

    this.id = id;
    this.name = name;
    this.description = description;
    this.date = date;
    this.favorite = favorite;
  }

  @Override
  public int describeContents() {

    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeString(id);
    dest.writeString(name);
    dest.writeString(description);
    dest.writeLong(this.date != null ? this.date.getTime() : -1);
    dest.writeStringArray(tags);
    dest.writeByte((byte) (favorite ? 1 : 0));
    dest.writeValue(reminder);
    dest.writeValue(reminderUnit);
    dest.writeInt(timeLapse);
    dest.writeValue(timeLapseUnit);
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

    EventViewModel event = (EventViewModel) o;

    return Objects.equals(id, event.id);
  }

  public String[] getTags() {

    return tags.clone();
  }

  public void setTags(String[] tags) {

    this.tags = tags.clone();
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

  @NonNull
  public TimeUnit getReminderUnit() {

    return reminderUnit != null ? reminderUnit : TimeUnit.DAY;
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

  @NonNull
  public TimeUnit getTimeLapseUnit() {

    return timeLapseUnit != null ? timeLapseUnit : TimeUnit.DAY;
  }

  public void setTimeLapseUnit(TimeUnit timeLapseUnit) {

    this.timeLapseUnit = timeLapseUnit;
  }

  public boolean hasReminder() {

    return reminder != null;
  }

  public boolean hasTimeLapseReset() {

    return timeLapse != 0;
  }

  @Override
  public EventViewModel clone() {

    EventViewModel obj = null;
    try {
      obj = (EventViewModel) super.clone();
    } catch (CloneNotSupportedException ex) {
      Timber.w("Cannot be cloned");
    }
    return obj;
  }
}
