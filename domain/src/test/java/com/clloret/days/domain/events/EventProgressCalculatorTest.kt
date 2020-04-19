package com.clloret.days.domain.events

import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.entities.EventBuilder
import com.clloret.days.domain.utils.TimeProvider
import com.google.common.truth.Truth.assertThat
import org.joda.time.LocalDate
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EventProgressCalculatorTest {

  private val today = LocalDate()
          .withYear(2020)
          .withMonthOfYear(4)
          .withDayOfMonth(6)

  private val progressDate1 = LocalDate()
          .withDayOfMonth(1)
          .withMonthOfYear(4)
          .withYear(2020)
          .toDate()

  private val progressDate2 = LocalDate()
          .withDayOfMonth(16)
          .withMonthOfYear(4)
          .withYear(2020)
          .toDate()

  private val latestDate = LocalDate()
          .withDayOfMonth(30)
          .withMonthOfYear(4)
          .withYear(2020)
          .toDate()

  private val oldestDate = LocalDate()
          .withDayOfMonth(1)
          .withMonthOfYear(4)
          .withYear(2020)
          .toDate()

  private var futureEvent = EventBuilder()
          .setDate(latestDate)
          .build()

  private var pastEvent = EventBuilder()
          .setDate(oldestDate)
          .build()

  @Mock
  lateinit var timeProvider: TimeProvider

  @InjectMocks
  lateinit var sut: EventProgressCalculator

  @Before
  fun init() {
    MockitoAnnotations.initMocks(this)

    Mockito.`when`(timeProvider.currentDate).thenReturn(today)
  }

  @Test
  fun calculateEventProgress_WhenDateIsAfter_ReturnCorrectValues() {
    futureEvent.progressDate = progressDate1

    val progress = sut.calculateEventProgress(futureEvent)

    assertThat(progress.progress).isEqualTo(5)
    assertThat(progress.max).isEqualTo(29)
  }

  @Test
  fun calculateEventProgress_WhenDateIsBefore_ReturnCorrectValues() {
    pastEvent.progressDate = progressDate2

    val progress = sut.calculateEventProgress(pastEvent)

    assertThat(progress.progress).isEqualTo(5)
    assertThat(progress.max).isEqualTo(15)
  }

  @Test
  fun calculateEventProgressDate_WhenDateIsBeforeAndHasReminder_ReturnCorrectValues() {
    val event = EventBuilder()
            .setDate(oldestDate)
            .setReminder(29)
            .setReminderUnit(Event.TimeUnit.DAY)
            .build()

    val date = sut.calculateEventProgressDate(event)

    assertThat(date).isEqualTo(latestDate)
  }

  @Test
  fun calculateEventProgressDate_WhenDateIsAfter_ReturnCorrectValues() {
    val event = EventBuilder()
            .setDate(latestDate)
            .build()

    val date = sut.calculateEventProgressDate(event)

    assertThat(date).isEqualTo(today.toDate())
  }

  @Test
  fun calculateEventProgressDate_WhenDateIsBeforeAndNoReminder_ReturnNullDate() {
    val event = EventBuilder()
            .setDate(oldestDate)
            .build()

    val date = sut.calculateEventProgressDate(event)

    assertThat(date).isNull()
  }

}