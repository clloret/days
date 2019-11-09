package com.clloret.days.model.entities;

import android.os.Parcel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TagViewModel implements android.os.Parcelable {

  public static final Creator<TagViewModel> CREATOR = new Creator<TagViewModel>() {
    @Override
    public TagViewModel createFromParcel(Parcel source) {

      return new TagViewModel(source);
    }

    @Override
    public TagViewModel[] newArray(int size) {

      return new TagViewModel[size];
    }
  };

  private String id;

  private String name;

  // empty constructor needed by the Parceler library
  public TagViewModel() {

  }

  public TagViewModel(@Nullable String id, String name) {

    this.id = id;
    this.name = name;
  }

  public TagViewModel(Parcel in) {

    this.id = in.readString();
    this.name = in.readString();
  }

  public TagViewModel(String id) {

    this.id = id;
  }

  @NonNull
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

  @Override
  public int describeContents() {

    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {

    dest.writeString(this.id);
    dest.writeString(this.name);
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

    TagViewModel tag = (TagViewModel) o;

    return id.equals(tag.id);
  }

  @NonNull
  @Override
  public String toString() {

    return name;
  }
}
