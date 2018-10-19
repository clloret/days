package com.clloret.days.domain.entities;

//import android.os.Parcel;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class Tag
//    implements android.os.Parcelable
{

//  public static final Creator<Tag> CREATOR = new Creator<Tag>() {
//    @Override
//    public Tag createFromParcel(Parcel source) {
//
//      return new Tag(source);
//    }
//
//    @Override
//    public Tag[] newArray(int size) {
//
//      return new Tag[size];
//    }
//  };

  private String id;

  private String name;

  // empty constructor needed by the Parceler library
  public Tag() {

  }

  public Tag(@Nullable String id, String name) {

    this.id = id;
    this.name = name;
  }

//  public Tag(Parcel in) {
//
//    this.id = in.readString();
//    this.name = in.readString();
//  }

  public Tag(String id) {

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

//  @Override
//  public int describeContents() {
//
//    return 0;
//  }

//  @Override
//  public void writeToParcel(Parcel dest, int flags) {
//
//    dest.writeString(this.id);
//    dest.writeString(this.name);
//  }

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
