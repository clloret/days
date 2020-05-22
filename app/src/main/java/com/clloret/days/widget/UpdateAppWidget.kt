package com.clloret.days.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.clloret.days.R
import com.clloret.days.activities.MainActivity
import com.clloret.days.device.notifications.NotificationsIntents
import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.events.EventPeriodFormat
import com.clloret.days.domain.interactors.events.GetEventUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class UpdateAppWidget @Inject constructor(
        private val getEventUseCase: GetEventUseCase,
        private val eventPeriodFormat: EventPeriodFormat,
        private val notificationsIntents: NotificationsIntents
) {

  @SuppressLint("CheckResult")
  fun update(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

    loadEventIdPref(context, appWidgetId)?.apply {
      getEventUseCase.execute(this)
              .subscribeOn(AndroidSchedulers.mainThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe({ event: Event ->

                val daysSinceFormatted = eventPeriodFormat.getDaysSinceFormatted(event.date)
                val pendingIntent = notificationsIntents.getViewEventIntent(event)

                showWidgetValues(daysSinceFormatted, event.name, pendingIntent, context,
                        appWidgetManager, appWidgetId)
              })
              { Timber.e(it) }
    } ?: run {
      val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
              .let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
              }

      showWidgetValues(UNKNOWN_EVENT_DAYS, UNKNOWN_EVENT_NAME, pendingIntent, context,
              appWidgetManager, appWidgetId)
    }
  }

  private fun showWidgetValues(days: String, eventName: String, pendingIntent: PendingIntent,
                               context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

    RemoteViews(context.packageName, R.layout.days_widget).apply {
      setTextViewText(R.id.textViewEventDays, days)
      setTextViewText(R.id.textViewEventName, eventName)
      setOnClickPendingIntent(R.id.layoutWidget, pendingIntent)
      appWidgetManager.updateAppWidget(appWidgetId, this)
    }
  }

  companion object {
    private const val UNKNOWN_EVENT_DAYS = "0"
    private const val UNKNOWN_EVENT_NAME = "Error"
  }
}
