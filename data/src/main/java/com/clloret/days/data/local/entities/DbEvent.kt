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

  @ColumnInfo(name = "name")
  var name: String = ""

  @ColumnInfo(name = "description")
  var description: String? = ""

  @ColumnInfo(name = "date")
  var date: Date? = null

  @ColumnInfo(name = "tag_id")
  var tags: Array<String> = EMPTY_ARRAY

  @ColumnInfo(name = "favorite")
  var isFavorite = false

  @ColumnInfo(name = "reminder")
  var reminder: Int? = null

  @ColumnInfo(name = "reminder_unit")
  @TypeConverters(TimeUnitConverter::class)
  var reminderUnit: Event.TimeUnit = Event.TimeUnit.DAY

  @ColumnInfo(name = "time_lapse")
  var timeLapse = 0

  @ColumnInfo(name = "time_lapse_unit")
  @TypeConverters(TimeUnitConverter::class)
  var timeLapseUnit: Event.TimeUnit = Event.TimeUnit.DAY

  @ColumnInfo(name = "progress_date")
  var progressDate: Date? = null

  constructor(id: String,
              name: String,
              description: String?,
              date: Date?,
              favorite: Boolean,
              reminder: Int?,
              reminderUnit: Event.TimeUnit,
              timeLapse: Int,
              timeLapseUnit: Event.TimeUnit) {
    this.id = id
    this.name = name
    this.description = description
    this.date = date
    isFavorite = favorite
    this.reminder = reminder
    this.reminderUnit = reminderUnit
    this.timeLapse = timeLapse
    this.timeLapseUnit = timeLapseUnit
  }

  @Ignore
  constructor(id: String) {
    this.id = id
  }

  companion object {
    private val EMPTY_ARRAY = emptyArray<String>()
  }
}