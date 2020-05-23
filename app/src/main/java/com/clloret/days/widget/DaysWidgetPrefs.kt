package com.clloret.days.widget

import android.content.Context

class DaysWidgetPrefs {
  companion object {
    private const val PREFS_NAME = "com.clloret.days.widget.DaysWidget"
    private const val PREF_PREFIX_KEY = "appwidget_"

    // Write the prefix to the SharedPreferences object for this widget
    internal fun saveEventIdPref(context: Context, appWidgetId: Int, text: String) {
      val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
      prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
      prefs.apply()
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    internal fun loadEventIdPref(context: Context, appWidgetId: Int): String? {
      val prefs = context.getSharedPreferences(PREFS_NAME, 0)
      return prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    }

    internal fun deleteEventIdPref(context: Context, appWidgetId: Int) {
      val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
      prefs.remove(PREF_PREFIX_KEY + appWidgetId)
      prefs.apply()
    }
  }
}