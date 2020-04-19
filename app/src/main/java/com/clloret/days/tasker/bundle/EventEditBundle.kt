package com.clloret.days.tasker.bundle

import android.os.Bundle
import com.clloret.days.BuildConfig
import com.clloret.days.domain.utils.StringUtils.isNullOrEmpty
import timber.log.Timber

class EventEditBundle @JvmOverloads constructor(bundle: Bundle? = null) {
  private val bundle: Bundle

  var id: String?
    get() = bundle.getString(EXTRA_ID)
    set(id) {
      bundle.putString(EXTRA_ID, id)
    }

  var name: String?
    get() = bundle.getString(EXTRA_NAME)
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
    return "EventEditBundle{bundle=$bundle}"
  }

  companion object {
    const val BUNDLE_ID = "com.clloret.days.edit"
    const val EXTRA_ID = "com.clloret.days.edit.STRING_ID"
    const val EXTRA_NAME = "com.clloret.days.edit.STRING_NAME"
    const val EXTRA_DESCRIPTION = "com.clloret.days.edit.STRING_DESCRIPTION"
    const val EXTRA_DATE = "com.clloret.days.edit.STRING_DATE"
    const val EXTRA_REMINDER = "com.clloret.days.edit.STRING_REMINDER"
    const val EXTRA_FAVORITE = "com.clloret.days.edit.STRING_FAVORITE"
    private const val EXTRA_VERSION_CODE = "com.clloret.days.edit.INT_VERSION_CODE"

    fun isBundleValid(bundle: Bundle): Boolean {

      if (bundle.getString(CommonBundle.EXTRA_BUNDLE) != BUNDLE_ID) {
        return false
      }

      if (bundle.getInt(EXTRA_VERSION_CODE, -1) == -1) {
        return false
      }

      if (isNullOrEmpty(bundle.getString(EXTRA_ID))) {
        Timber.e("Invalid %s", EXTRA_ID)
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
