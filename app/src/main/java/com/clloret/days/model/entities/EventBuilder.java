package com.clloret.days.model.entities;

import java.util.Date;

public class EventBuilder {

  private static final String[] EMPTY_ARRAY = new String[0];

  private String id;
  private String name;
  private String description;
  private Date date;
  private String[] tags = EMPTY_ARRAY;
  private boolean favorite;

  public EventBuilder setId(String id) {

    this.id = id;
    return this;
  }

  public EventBuilder setName(String name) {

    this.name = name;
    return this;
  }

  public EventBuilder setDescription(String description) {

    this.description = description;
    return this;
  }

  public EventBuilder setDate(Date date) {

    this.date = date;
    return this;
  }

  public EventBuilder setTags(String[] tags) {

    this.tags = tags;
    return this;
  }

  public EventBuilder setFavorite(boolean favorite) {

    this.favorite = favorite;
    return this;
  }

  public Event build() {

    return new Event(id, name, description, date, tags, favorite);
  }
}