package com.clloret.days.domain.events

import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.utils.TimeProvider
import org.joda.time.LocalDate
import org.joda.time.Period
import org.joda.time.PeriodType
import java.util.*
import javax.inject.Inject

class EventProgressCalculator @Inject constructor(private val timeProvider: TimeProvider) {

  data class ProgressValue(val progress: Int = 0, val max: Int = 0)

  fun calculateEventProgress(event: Event): ProgressValue {

    val emptyProgress = ProgressValue()
    val progressDate = event.progressDate ?: return emptyProgress
    val eventDate = event.date ?: return emptyProgress

    val localEventDate = LocalDate(event.date)
    val futureDate = localEventDate.isAfter(timeProvider.currentDate)
    val max = getDays(eventDate, progressDate)
    val progress = getDays(if (futureDate) progressDate else eventDate, timeProvider.currentDate.toDate())

    return ProgressValue(progress = progress, max = max)
  }

  private fun getDays(fromDate: Date, toDate: Date): Int {
    val localFromDate = LocalDate(toDate)
    val localToDate = LocalDate(fromDate)
    val period = Period(
            if (localFromDate.isBefore(localToDate)) localFromDate else localToDate,
            if (localToDate.isAfter(localFromDate)) localToDate else localFromDate,
            PeriodType.days())

    return period.days
  }

}