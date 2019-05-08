package com.clloret.days.domain.entities;

import com.clloret.days.domain.tags.order.TagSortable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class Tag implements Identifiable, TagSortable {

  private String id;

  private String name;

  public Tag() {

  }

  public Tag(@Nullable String id, String name) {

    this.id = id;
    this.name = name;
  }

  public Tag(String id) {

    this.id = id;
  }

  @NonNull
  @Override
  public String getId() {

    return id;
  }

  @Override
  public void setId(String id) {

    this.id = id;
  }

  @Override
  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  @Override
  public int hashCode() {

    return id.hashCode();
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Tag tag = (Tag) o;

    return id.equals(tag.id);
  }

  @Override
  public String toString() {

    return name;
  }
}
