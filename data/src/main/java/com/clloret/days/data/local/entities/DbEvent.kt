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
  var name: String? = null

  @ColumnInfo(name = "description")
  var description: String? = null

  @ColumnInfo(name = "date")
  private var date: Date? = null

  @ColumnInfo(name = "tag_id")
  var tags = EMPTY_ARRAY

  @ColumnInfo(name = "favorite")
  var isFavorite = false

  @ColumnInfo(name = "reminder")
  var reminder: Int? = null

  @ColumnInfo(name = "reminder_unit")
  @TypeConverters(TimeUnitConverter::class)
  var reminderUnit: Event.TimeUnit? = null
    private set

  @ColumnInfo(name = "time_lapse")
  var timeLapse = 0

  @ColumnInfo(name = "time_lapse_unit")
  @TypeConverters(TimeUnitConverter::class)
  var timeLapseUnit: Event.TimeUnit? = null

  @ColumnInfo(name = "progress_date")
  var progressDate: Date? = null

  constructor(id: String, name: String?, description: String?, date: Date?, favorite: Boolean,
              reminder: Int?, reminderUnit: Event.TimeUnit?, timeLapse: Int, timeLapseUnit: Event.TimeUnit?) {
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

  fun getDate(): Date {
    return date!!.clone() as Date
  }

  fun setDate(date: Date) {
    this.date = date.clone() as Date
  }

  fun setReminderUnit(reminderUnit: Event.TimeUnit) {
    this.reminderUnit = reminderUnit
  }

  companion object {
    private val EMPTY_ARRAY = arrayOfNulls<String>(0)
  }
}