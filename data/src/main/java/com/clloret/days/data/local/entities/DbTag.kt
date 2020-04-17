package com.clloret.days.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
class DbTag {
  @PrimaryKey
  @ColumnInfo(name = "id")
  var id: String

  @ColumnInfo(name = "name", defaultValue = "")
  var name: String = ""

  constructor(id: String, name: String) {
    this.id = id
    this.name = name
  }

  @Ignore
  constructor(id: String) {
    this.id = id
  }

}