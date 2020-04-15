package com.clloret.days.tasker.bundle

import android.os.Bundle
import com.clloret.days.BuildConfig

class EventCreateBundle @JvmOverloads constructor(bundle: Bundle? = null) {
  private val bundle: Bundle

  var name: String
    get() = bundle.getString(EXTRA_NAME, "Unnamed")
    set(name) {
      bundle.putString(EXTRA_NAME, name)
    }

  var date: String?
    get() = bundle.getString(EXTRA_DATE)
    set(date) {
      bundle.putString(EXTRA_DATE, date)
    }

  var description: String?
    get() = bundle.getString(EXTRA_DESCRIPTION)
    set(description) {
      bundle.putString(EXTRA_DESCRIPTION, description)
    }

  var reminder: String?
    get() = bundle.getString(EXTRA_REMINDER)
    set(reminder) {
      bundle.putString(EXTRA_REMINDER, reminder)
    }

  var favorite: String?
    get() = bundle.getString(EXTRA_FAVORITE)
    set(favorite) {
      bundle.putString(EXTRA_FAVORITE, favorite)
    }

  fun build(): Bundle {
    bundle.putInt(EXTRA_VERSION_CODE, BuildConfig.VERSION_CODE)
    return bundle
  }

  override fun toString(): String {
    return "EventCreateBundle{bundle=$bundle}"
  }

  companion object {
    const val BUNDLE_ID = "com.clloret.days.create"
    const val EXTRA_NAME = "com.clloret.days.create.STRING_NAME"
    const val EXTRA_DESCRIPTION = "com.clloret.days.create.STRING_DESCRIPTION"
    const val EXTRA_DATE = "com.clloret.days.create.STRING_DATE"
    const val EXTRA_REMINDER = "com.clloret.days.create.STRING_REMINDER"
    const val EXTRA_FAVORITE = "com.clloret.days.create.STRING_FAVORITE"
    private const val EXTRA_VERSION_CODE = "com.clloret.days.create.INT_VERSION_CODE"

    fun isBundleValid(bundle: Bundle): Boolean {

      if (bundle.getString(CommonBundle.EXTRA_BUNDLE) != BUNDLE_ID) {
        return false
      }

      if (bundle.getInt(EXTRA_VERSION_CODE, -1) == -1) {
        return false
      }

      return true
    }
  }

  init {
    if (bundle == null) {
      this.bundle = Bundle()
      this.bundle.putInt(EXTRA_VERSION_CODE, BuildConfig.VERSION_CODE)
      this.bundle.putString(CommonBundle.EXTRA_BUNDLE, BUNDLE_ID)
    } else {
      this.bundle = bundle
    }
  }
}
