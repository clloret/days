package com.clloret.days.domain.entities

import com.clloret.days.domain.events.order.EventSortable
import org.slf4j.LoggerFactory
import java.util.*

class Event : Identifiable, EventSortable, Cloneable {
  private var id: String? = null
  private var name: String? = null
  var description: String? = null
  private var date: Date? = null
  var progressDate: Date? = null
  var tags = EMPTY_ARRAY
  private var favorite = false
  var reminder: Int? = null
  private var reminderUnit: TimeUnit? = TimeUnit.DAY
  var timeLapse = 0
  private var timeLapseUnit: TimeUnit? = TimeUnit.DAY

  constructor()
  constructor(id: String?, name: String?, description: String?, date: Date?, tags: Array<String?>,
              favorite: Boolean, reminder: Int?, reminderUnit: TimeUnit?, timeLapse: Int,
              timeLapseUnit: TimeUnit?, progressDate: Date?) {
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

  override fun getId(): String? {
    return id
  }

  override fun setId(id: String?) {
    this.id = id
  }

  override fun isFavorite(): Boolean {
    return favorite
  }

  override fun getName(): String? {
    return name
  }

  fun setName(name: String?) {
    this.name = name
  }

  override fun getDate(): Date? {
    return date
  }

  fun setDate(date: Date?) {
    this.date = if (date == null) null else date.clone() as Date
  }

  fun setFavorite(favorite: Boolean) {
    this.favorite = favorite
  }

  override fun hashCode(): Int {
    return if (id != null) id.hashCode() else 0
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other == null || javaClass != other.javaClass) {
      return false
    }
    val event = other as Event
    return id == event.id
  }

  public override fun clone(): Event {
    var obj: Event? = null
    try {
      obj = super.clone() as Event
    } catch (ex: CloneNotSupportedException) {
      logger.warn("Cannot be cloned")
    }
    return obj!!
  }

  fun hasReminder(): Boolean {
    return reminder != null
  }

  fun getReminderUnit(): TimeUnit {
    return (if (reminderUnit != null) reminderUnit!! else TimeUnit.DAY)
  }

  fun setReminderUnit(reminderUnit: TimeUnit) {
    this.reminderUnit = reminderUnit
  }

  fun getTimeLapseUnit(): TimeUnit {
    return (if (timeLapseUnit != null) timeLapseUnit!! else TimeUnit.DAY)
  }

  fun setTimeLapseUnit(timeLapseUnit: TimeUnit?) {
    this.timeLapseUnit = timeLapseUnit
  }

  enum class TimeUnit(private val text: String) {
    DAY("Day"), MONTH("Month"), YEAR("Year");

    override fun toString(): String {
      return text
    }

  }

  companion object {
    const val REMINDER_EVENT_DAY = 0
    private val logger = LoggerFactory.getLogger(Event::class.java.simpleName)
    private val EMPTY_ARRAY = arrayOfNulls<String>(0)
  }
}