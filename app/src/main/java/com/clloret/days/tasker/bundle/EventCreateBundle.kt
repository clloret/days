package com.clloret.days.tasker.bundle

import android.os.Bundle
import com.clloret.days.BuildConfig

class EventCreateBundle @JvmOverloads constructor(bundle: Bundle? = null) {
  private val bundle: Bundle

  var title: String?
    get() = bundle.getString(EXTRA_NAME)
    set(title) {
      bundle.putString(EXTRA_NAME, title)
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

  fun build(): Bundle {
    bundle.putInt(EXTRA_VERSION_CODE, BuildConfig.VERSION_CODE)
    return bundle
  }

  override fun toString(): String {
    return "TaskCreationBundle{bundle=$bundle}"
  }

  companion object {
    const val EXTRA_BUNDLE = "com.clloret.days.create"
    const val EXTRA_NAME = "com.clloret.days.create.STRING_NAME"
    const val EXTRA_DESCRIPTION = "com.clloret.days.create.STRING_DESCRIPTION"
    const val EXTRA_DATE = "com.clloret.days.create.STRING_DATE"
    const val EXTRA_REMINDER = "com.clloret.days.create.STRING_REMINDER"
    private const val EXTRA_VERSION_CODE = "com.clloret.days.create.INT_VERSION_CODE"

    fun isBundleValid(bundle: Bundle): Boolean {
      return -1 != bundle.getInt(EXTRA_VERSION_CODE, -1)
    }
  }

  init {
    if (bundle == null) {
      this.bundle = Bundle()
      this.bundle.putInt(EXTRA_VERSION_CODE, BuildConfig.VERSION_CODE)
    } else {
      this.bundle = bundle
    }
  }
}
