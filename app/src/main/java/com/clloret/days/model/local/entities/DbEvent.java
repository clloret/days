package com.clloret.days.model.local.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
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

  public DbEvent(@NonNull String id, String name, String description, Date date, boolean favorite) {

    this.id = id;
    this.name = name;
    this.description = description;
    this.date = date;
    this.favorite = favorite;
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
}
