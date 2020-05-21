package com.clloret.days.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [DaysWidgetConfigureActivity]
 */
class DaysWidget : AppWidgetProvider() {

  @Inject
  lateinit var updateAppWidget: UpdateAppWidget

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

    AndroidInjection.inject(this, context)

    for (appWidgetId in appWidgetIds) {
      updateAppWidget.update(context, appWidgetManager, appWidgetId)
    }
  }

  override fun onDeleted(context: Context, appWidgetIds: IntArray) {
    for (appWidgetId in appWidgetIds) {
      deleteTitlePref(context, appWidgetId)
    }
  }

  override fun onEnabled(context: Context) {
    // Enter relevant functionality for when the first widget is created
  }

  override fun onDisabled(context: Context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}
