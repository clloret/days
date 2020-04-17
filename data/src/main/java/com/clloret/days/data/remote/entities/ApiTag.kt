package com.clloret.days.data.remote.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiTag {
  @Expose(serialize = false)
  @Transient
  var id: String? = null

  @Expose(serialize = false)
  @Transient
  var createdTime: String? = null

  @SerializedName("Name")
  var name: String = ""

  // empty constructor needed by the Airtable library
  constructor()
  constructor(id: String?, name: String) {
    this.id = id
    this.name = name
  }

}