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

    val eventId = loadTitlePref(context, appWidgetId)

    getEventUseCase.execute(eventId)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ event: Event ->

              val views = RemoteViews(context.packageName, R.layout.days_widget)

              val daysSinceFormatted = eventPeriodFormat.getDaysSinceFormatted(event.date)
              views.setTextViewText(R.id.textViewEventDays, daysSinceFormatted)

              views.setTextViewText(R.id.textViewEventName, event.name)

              appWidgetManager.updateAppWidget(appWidgetId, views)
            })
            { Timber.e(it) }
  }
}