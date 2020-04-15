package com.clloret.days.data.local.entities

import androidx.room.*
import com.clloret.days.data.local.entities.converters.TimeUnitConverter
import com.clloret.days.domain.entities.Event
import java.util.*

@Entity(tableName = "events")
class DbEvent {
  @PrimaryKey
  @ColumnInfo(name = "id")
  var id: String

  @ColumnInfo(name = "name", defaultValue = "")
  var name: String = ""

  @ColumnInfo(name = "description")
  var description: String? = null

  @ColumnInfo(name = "date")
  var date: Date = Date()

  @ColumnInfo(name = "tag_id")
  var tags: Array<String> = EMPTY_ARRAY

  @ColumnInfo(name = "favorite")
  var favorite = false

  @ColumnInfo(name = "reminder")
  var reminder: Int? = null

  @ColumnInfo(name = "reminder_unit", defaultValue = "Day")
  @TypeConverters(TimeUnitConverter::class)
  var reminderUnit: Event.TimeUnit = Event.TimeUnit.DAY

  @ColumnInfo(name = "time_lapse")
  var timeLapse = 0

  @ColumnInfo(name = "time_lapse_unit", defaultValue = "Day")
  @TypeConverters(TimeUnitConverter::class)
  var timeLapseUnit: Event.TimeUnit = Event.TimeUnit.DAY

  @ColumnInfo(name = "progress_date")
  var progressDate: Date? = null

  @JvmOverloads
  constructor(id: String,
              name: String,
              description: String?,
              date: Date,
              tags: Array<String> = EMPTY_ARRAY,
              favorite: Boolean,
              reminder: Int?,
              reminderUnit: Event.TimeUnit,
              timeLapse: Int,
              timeLapseUnit: Event.TimeUnit,
              progressDate: Date? = null) {
    this.id = id
    this.name = name
    this.description = description
    this.date = date
    this.tags = tags
    this.favorite = favorite
    this.reminder = reminder
    this.reminderUnit = reminderUnit
    this.timeLapse = timeLapse
    this.timeLapseUnit = timeLapseUnit
    this.progressDate = progressDate
  }

  @Ignore
  constructor(id: String) {
    this.id = id
  }

  companion object {
    private val EMPTY_ARRAY = emptyArray<String>()
  }
}