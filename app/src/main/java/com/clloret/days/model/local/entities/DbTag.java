package com.clloret.days.model.local.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tags")
public class DbTag {

  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  private String id;

  @ColumnInfo(name = "name")
  private String name;

  public DbTag(@NonNull String id, String name) {

    this.id = id;
    this.name = name;
  }

  @Ignore
  public DbTag(@NonNull String id) {

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
}
