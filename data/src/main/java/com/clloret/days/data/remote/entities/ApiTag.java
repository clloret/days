package com.clloret.days.data.remote.entities;

import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiTag {

  @Expose(serialize = false)
  private transient String id;

  @SerializedName("Name")
  private String name;

  // empty constructor needed by the Airtable library
  @SuppressWarnings("unused")
  public ApiTag() {

  }

  public ApiTag(String id) {

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
}
