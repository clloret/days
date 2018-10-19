package com.clloret.days.data.remote.entities;

import android.support.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class ApiEvent {

  private static final String[] EMPTY_ARRAY = new String[0];

  @Expose(serialize = false)
  private String id;

  @SerializedName("Name")
  private String name;

  @SerializedName("Description")
  private String description;

  @SerializedName("Date")
  private Date date;

  @SerializedName("Tags")
  private String[] tags = EMPTY_ARRAY;

  @SerializedName("Favorite")
  private boolean favorite;

  private String createdTime;

  // empty constructor needed by the Airtable library
  @SuppressWarnings("unused")
  public ApiEvent() {

  }

  public ApiEvent(@Nullable String id) {

    this.id = id;
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

  public String getCreatedTime() {

    return createdTime;
  }

  public void setCreatedTime(String createdTime) {

    this.createdTime = createdTime;
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
