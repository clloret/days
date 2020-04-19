package com.clloret.days.model.entities

import android.os.Parcel
import android.os.Parcelable
import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.events.order.EventSortable
import timber.log.Timber
import java.util.*

// If replace Parcelable, maintain field "event" implementation for Date type
data class EventViewModel(var id: String? = null,
                          override var name: String = "",
                          var description: String? = null,
                          override var date: Date = Date(),
                          var tags: Array<String> = Event.EMPTY_ARRAY,
                          var favorite: Boolean = false,
                          var reminder: Int? = null,
                          var reminderUnit: Event.TimeUnit = Event.TimeUnit.DAY,
                          var timeLapse: Int = 0,
                          var timeLapseUnit: Event.TimeUnit = Event.TimeUnit.DAY,
                          var progressDate: Date? = null) : Parcelable, Cloneable, EventSortable {

  constructor(parcel: Parcel) : this(
          id = parcel.readString(),
          name = parcel.readString() ?: "",
          description = parcel.readString(),
          date = getDateFromLong(parcel.readLong()) ?: Date(),
          tags = parcel.createStringArray() as Array<String>,
          favorite = parcel.readByte().toInt() != 0,
          reminder = parcel.readValue(Int::class.java.classLoader) as Int?,
          reminderUnit = parcel.readValue(Event.TimeUnit::class.java.classLoader) as Event.TimeUnit,
          timeLapse = parcel.readInt(),
          timeLapseUnit = parcel.readValue(Event.TimeUnit::class.java.classLoader) as Event.TimeUnit,
          progressDate = getDateFromLong(parcel.readLong()))

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(id)
    dest.writeString(name)
    dest.writeString(description)
    dest.writeLong(date.time)
    dest.writeStringArray(tags)
    dest.writeByte((if (favorite) 1 else 0).toByte())
    dest.writeValue(reminder)
    dest.writeValue(reminderUnit)
    dest.writeInt(timeLapse)
    dest.writeValue(timeLapseUnit)
    dest.writeLong(if (progressDate != null) progressDate!!.time else -1)
  }

  override fun describeContents(): Int {
    return 0
  }

  override val isFavorite: Boolean
    get() = favorite

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
    return name
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }

    if (other == null || other !is EventViewModel) return false

    return id == other.id
  }

  override fun hashCode(): Int {
    return if (id != null) id.hashCode() else 0
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

    private fun getDateFromLong(number: Long): Date? {
      return if (number == -1L) null else Date(number)
    }
  }

}