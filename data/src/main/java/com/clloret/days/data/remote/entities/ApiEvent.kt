package com.clloret.days.data.remote.entities

import com.clloret.days.domain.entities.Event
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

@Suppress("unused")
class ApiEvent {
  @Expose(serialize = false)
  @Transient
  var id: String? = null

  @Expose(serialize = false)
  @Transient
  var createdTime: String? = null

  @SerializedName("Name")
  var name: String = ""

  @SerializedName("Description")
  var description: String? = null

  @SerializedName("Date")
  private var date: Date? = null

  @SerializedName("Tags")
  var tags: Array<String> = EMPTY_ARRAY

  @SerializedName("Favorite")
  var favorite = false

  @SerializedName("Reminder")
  var reminder: Int? = null

  @SerializedName("ReminderUnit")
  var reminderUnit: String? = null
    private set

  @SerializedName("TimeLapse")
  var timeLapse = 0

  @SerializedName("TimeLapseUnit")
  var timeLapseUnit: String? = null

  @SerializedName("ProgressDate")
  var progressDate: Date? = null

  // empty constructor needed by the Airtable library
  constructor()
  constructor(id: String?) {
    this.id = id
  }

  constructor(id: String?,
              name: String,
              description: String?,
              date: Date?,
              tags: Array<String>,
              favorite: Boolean,
              reminder: Int?,
              reminderTimeUnit: Event.TimeUnit,
              timeLapse: Int,
              timeLapseTimeUnit: Event.TimeUnit,
              progressDate: Date?) {
    this.id = id
    this.name = name
    this.description = description
    this.date = date
    this.tags = tags
    this.favorite = favorite
    this.reminder = reminder
    this.reminderTimeUnit = reminderTimeUnit
    this.timeLapse = timeLapse
    this.timeLapseTimeUnit = timeLapseTimeUnit
    this.progressDate = progressDate
  }

  fun getDate(): Date {
    return date!!.clone() as Date
  }

  fun setDate(date: Date?) {
    this.date = date?.clone() as Date
  }

  fun setReminderUnit(reminderUnit: String) {
    this.reminderUnit = reminderUnit
  }

  var reminderTimeUnit: Event.TimeUnit
    get() = if (reminderUnit == null) Event.TimeUnit.DAY else
      Event.TimeUnit.valueOf(reminderUnit!!.toUpperCase(Locale.ROOT))
    set(reminderUnit) {
      this.reminderUnit = reminderUnit.toString()
    }

  var timeLapseTimeUnit: Event.TimeUnit
    get() = if (timeLapseUnit == null) Event.TimeUnit.DAY else
      Event.TimeUnit.valueOf(timeLapseUnit!!.toUpperCase(Locale.ROOT))
    set(timeLapseUnit) {
      this.timeLapseUnit = timeLapseUnit.toString()
    }

  companion object {
    private val EMPTY_ARRAY = emptyArray<String>()
  }
}