package com.clloret.days.model.entities

import android.os.Parcel
import android.os.Parcelable
import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.events.order.EventSortable
import timber.log.Timber
import java.util.*

// If replace Parcelable, maintain field "event" implementation for Date type
class EventViewModel : Parcelable, Cloneable, EventSortable {
  var id: String? = null
  private var name: String? = null
  var description: String? = null
  private var date: Date? = null
  private var tags: Array<String?>? = EMPTY_ARRAY
  private var favorite = false
  var reminder: Int? = null
  private var reminderUnit: Event.TimeUnit? = null
  var timeLapse = 0
  private var timeLapseUnit: Event.TimeUnit? = null
  var progressDate: Date? = null

  // empty constructor needed by the Parceler library
  constructor()
  constructor(parcel: Parcel) {
    id = parcel.readString()
    name = parcel.readString()
    description = parcel.readString()
    val tmpDate = parcel.readLong()
    date = if (tmpDate == -1L) null else Date(tmpDate)
    tags = parcel.createStringArray()
    favorite = parcel.readByte().toInt() != 0
    reminder = parcel.readValue(Int::class.java.classLoader) as Int?
    reminderUnit = parcel.readValue(Event.TimeUnit::class.java.classLoader) as Event.TimeUnit?
    timeLapse = parcel.readInt()
    timeLapseUnit = parcel.readValue(Event.TimeUnit::class.java.classLoader) as Event.TimeUnit?
    val tmpProgressDate = parcel.readLong()
    progressDate = if (tmpProgressDate == -1L) null else Date(tmpProgressDate)
  }

  constructor(id: String?, name: String?, description: String?, date: Date?, tags: Array<String?>,
              favorite: Boolean) {
    this.id = id
    this.name = name
    this.description = description
    this.date = date
    this.tags = tags.clone()
    this.favorite = favorite
  }

  constructor(id: String?, name: String?, description: String?, date: Date?, favorite: Boolean) {
    this.id = id
    this.name = name
    this.description = description
    this.date = date
    this.favorite = favorite
  }

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(id)
    dest.writeString(name)
    dest.writeString(description)
    dest.writeLong(if (date != null) date!!.time else -1)
    dest.writeStringArray(tags)
    dest.writeByte((if (favorite) 1 else 0).toByte())
    dest.writeValue(reminder)
    dest.writeValue(reminderUnit)
    dest.writeInt(timeLapse)
    dest.writeValue(timeLapseUnit)
    dest.writeLong(if (progressDate != null) progressDate!!.time else -1)
  }

  override fun isFavorite(): Boolean {
    return favorite
  }

  override fun getName(): String {
    return name!!
  }

  fun setName(name: String?) {
    this.name = name
  }

  override fun getDate(): Date {
    return (if (date == null) null else date!!.clone() as Date)!!
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
    val event = other as EventViewModel
    return id == event.id
  }

  public override fun clone(): EventViewModel {
    var obj: EventViewModel? = null
    try {
      obj = super.clone() as EventViewModel
    } catch (ex: CloneNotSupportedException) {
      Timber.w("Cannot be cloned")
    }
    return obj!!
  }

  override fun toString(): String {
    return name!!
  }

  fun getTags(): Array<String?> {
    return tags!!.clone()
  }

  fun setTags(tags: Array<String?>) {
    this.tags = tags.clone()
  }

  fun getReminderUnit(): Event.TimeUnit {
    return (if (reminderUnit != null) reminderUnit!! else Event.TimeUnit.DAY)
  }

  fun setReminderUnit(reminderUnit: Event.TimeUnit) {
    this.reminderUnit = reminderUnit
  }

  fun getTimeLapseUnit(): Event.TimeUnit {
    return (if (timeLapseUnit != null) timeLapseUnit!! else Event.TimeUnit.DAY)
  }

  fun setTimeLapseUnit(timeLapseUnit: Event.TimeUnit?) {
    this.timeLapseUnit = timeLapseUnit
  }

  fun hasReminder(): Boolean {
    return reminder != null
  }

  fun hasTimeLapseReset(): Boolean {
    return timeLapse != 0
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<EventViewModel> = object : Parcelable.Creator<EventViewModel> {
      override fun createFromParcel(parcel: Parcel): EventViewModel {
        return EventViewModel(parcel)
      }

      override fun newArray(size: Int): Array<EventViewModel?> {
        return arrayOfNulls(size)
      }
    }
    private val EMPTY_ARRAY = arrayOfNulls<String>(0)
  }
}