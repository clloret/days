package com.clloret.days.domain.entities

import com.clloret.days.domain.events.order.EventSortable
import org.slf4j.LoggerFactory
import java.util.*

data class Event(override var id: String? = null,
                 override var name: String = "",
                 var description: String? = null,
                 override var date: Date = Date(),
                 var tags: Array<String> = EMPTY_ARRAY,
                 var favorite: Boolean = false,
                 var reminder: Int? = null,
                 var reminderUnit: TimeUnit = TimeUnit.DAY,
                 var timeLapse: Int = 0,
                 var timeLapseUnit: TimeUnit = TimeUnit.DAY,
                 var progressDate: Date? = null) : Identifiable, EventSortable, Cloneable {

  override val isFavorite: Boolean
    get() = favorite

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

  enum class TimeUnit(private val text: String) {
    DAY("Day"), MONTH("Month"), YEAR("Year");

    override fun toString(): String {
      return text
    }

  }

  companion object {
    const val REMINDER_EVENT_DAY = 0
    private val logger = LoggerFactory.getLogger(Event::class.java.simpleName)
    private val EMPTY_ARRAY = emptyArray<String>()
  }
}