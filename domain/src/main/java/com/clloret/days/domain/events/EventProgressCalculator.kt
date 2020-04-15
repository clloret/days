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

  fun setDefaultProgressDate(event: Event) {
    val date = calculateEventProgressDate(event)
    event.progressDate = date
  }

  fun calculateEventProgress(event: Event): ProgressValue {

    val emptyProgress = ProgressValue()
    val progressDate = event.progressDate ?: return emptyProgress
    val eventDate = event.date

    val localEventDate = LocalDate(event.date)
    val futureDate = localEventDate.isAfter(timeProvider.currentDate)
    val max = getDays(eventDate, progressDate)
    val progress = getDays(if (futureDate) progressDate else eventDate, timeProvider.currentDate.toDate())

    return ProgressValue(progress = progress, max = max)
  }

  fun calculateEventProgressDate(event: Event): Date? {
    val localFromDate = LocalDate(event.date)
    if (localFromDate.isAfter(timeProvider.currentDate)) {
      return timeProvider.currentDate.toDate()
    } else if (event.hasReminder()) {
      return calculateTimeReminder(event, event.date)
    }
    return null
  }

  private fun calculateTimeReminder(event: Event, date: Date?): Date? {

    val reminder = checkNotNull(event.reminder) { "Event reminder can't be null" }
    val localDate = LocalDate(date)
    val dateReminder: LocalDate
    dateReminder = when (event.reminderUnit) {
      Event.TimeUnit.DAY -> localDate.plusDays(reminder)
      Event.TimeUnit.MONTH -> localDate.plusMonths(reminder)
      Event.TimeUnit.YEAR -> localDate.plusYears(reminder)
    }
    return dateReminder.toDate()
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