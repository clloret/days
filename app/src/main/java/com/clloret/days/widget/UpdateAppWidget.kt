package com.clloret.days.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import com.clloret.days.R
import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.events.EventPeriodFormat
import com.clloret.days.domain.interactors.events.GetEventUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class UpdateAppWidget @Inject constructor(
        private val getEventUseCase: GetEventUseCase,
        private val eventPeriodFormat: EventPeriodFormat) {

  @SuppressLint("CheckResult")
  fun update(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

    loadEventIdPref(context, appWidgetId)?.apply {
      getEventUseCase.execute(this)
              .subscribeOn(AndroidSchedulers.mainThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({ event: Event ->

                val daysSinceFormatted = eventPeriodFormat.getDaysSinceFormatted(event.date)

                showWidgetValues(daysSinceFormatted, event.name, context, appWidgetManager, appWidgetId)
              })
              { Timber.e(it) }
    } ?: run {
      showWidgetValues(UNKNOWN_EVENT_DAYS, UNKNOWN_EVENT_NAME, context, appWidgetManager, appWidgetId)
    }

  }

  private fun showWidgetValues(days: String?, eventName: String, context: Context,
                               appWidgetManager: AppWidgetManager, appWidgetId: Int) {

    RemoteViews(context.packageName, R.layout.days_widget).apply {
      setTextViewText(R.id.textViewEventDays, days)
      setTextViewText(R.id.textViewEventName, eventName)
      appWidgetManager.updateAppWidget(appWidgetId, this)
    }
  }

  companion object {
    private const val UNKNOWN_EVENT_DAYS = "0"
    private const val UNKNOWN_EVENT_NAME = "Error"
  }
}
