package com.clloret.days.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.clloret.days.R
import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.interactors.events.GetEventUseCase
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [DaysWidgetConfigureActivity]
 */
class DaysWidget : AppWidgetProvider() {

  @Inject
  lateinit var getEventUseCase: GetEventUseCase

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

    AndroidInjection.inject(this, context)

    // There may be multiple widgets active, so update all of them
    for (appWidgetId in appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId, getEventUseCase)
    }
  }

  override fun onDeleted(context: Context, appWidgetIds: IntArray) {
    // When the user deletes the widget, delete the preference associated with it.
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

@SuppressLint("CheckResult")
internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int,
                             getEventUseCase: GetEventUseCase) {

  val eventId = loadTitlePref(context, appWidgetId)

  getEventUseCase.execute(eventId)
          .subscribeOn(AndroidSchedulers.mainThread())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({ event: Event ->

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.days_widget)
            views.setTextViewText(R.id.appwidget_text, event.name)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
          })
          { Timber.e(it) }
}