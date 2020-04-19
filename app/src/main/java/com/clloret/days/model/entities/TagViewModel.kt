package com.clloret.days.model.entities

import android.os.Parcel
import android.os.Parcelable
import com.clloret.days.domain.tags.order.TagSortable

data class TagViewModel(var id: String? = null,
                        override var name: String = "") : Parcelable, TagSortable {

  constructor(parcel: Parcel) : this(
          id = parcel.readString(),
          name = parcel.readString() ?: "")

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(id)
    dest.writeString(name)
  }

  override fun toString(): String {
    return name
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<TagViewModel> = object : Parcelable.Creator<TagViewModel> {
      override fun createFromParcel(parcel: Parcel): TagViewModel {
        return TagViewModel(parcel)
      }

      override fun newArray(size: Int): Array<TagViewModel?> {
        return arrayOfNulls(size)
      }
    }
  }
}