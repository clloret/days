package com.clloret.days.model.entities.mapper

import com.clloret.days.domain.entities.Event
import com.clloret.days.model.entities.EventViewModel
import com.google.common.truth.Truth
import org.joda.time.LocalDate
import org.junit.Test

class EventViewModelMapperKtTest {

  @Test
  fun toEventViewModel_Always_MapAllValues() {
    val event = buildEvent()
    val viewModel = event.toEventViewModel()
    assertEventViewModel(viewModel, event)
  }

  @Test
  fun toEvent_Always_MapAllValues() {
    val viewModel = buildEventViewModel()
    val event = viewModel.toEvent()
    assertEvent(event, viewModel)
  }

  @Test
  fun toEventViewModelList_Always_MapAllValues() {
    val event = buildEvent()
    val eventList = arrayListOf(event, event)
    val toEventViewModelList = eventList.toEventViewModelList()

    toEventViewModelList.forEach {
      assertEventViewModel(it, event)
    }

  }

  private fun buildEvent(): Event {
    val date = LocalDate()
            .withYear(2020)
            .withMonthOfYear(6)
            .withDayOfMonth(15)
            .toDate()

    return Event(
            id = "id",
            name = "name",
            description = "description",
            date = date,
            tags = arrayOf("One", "Two"),
            favorite = true,
            reminder = 5,
            reminderUnit = Event.TimeUnit.MONTH,
            timeLapse = 10,
            timeLapseUnit = Event.TimeUnit.YEAR,
            progressDate = date)
  }

  private fun buildEventViewModel(): EventViewModel {
    val date = LocalDate()
            .withYear(2020)
            .withMonthOfYear(6)
            .withDayOfMonth(15)
            .toDate()

    return EventViewModel(
            id = "id",
            name = "name",
            description = "description",
            date = date,
            tags = arrayOf("One", "Two"),
            favorite = true,
            reminder = 5,
            reminderUnit = Event.TimeUnit.MONTH,
            timeLapse = 10,
            timeLapseUnit = Event.TimeUnit.YEAR,
            progressDate = date)
  }

  private fun assertEventViewModel(vm: EventViewModel, event: Event) {
    Truth.assertThat(vm.id).isEqualTo(event.id)
    Truth.assertThat(vm.name).isEqualTo(event.name)
    Truth.assertThat(vm.description).isEqualTo(event.description)
    Truth.assertThat(vm.date).isEqualTo(event.date)
    Truth.assertThat(vm.tags).isEqualTo(event.tags)
    Truth.assertThat(vm.favorite).isEqualTo(event.favorite)
    Truth.assertThat(vm.reminder).isEqualTo(event.reminder)
    Truth.assertThat(vm.reminderUnit).isEqualTo(event.reminderUnit)
    Truth.assertThat(vm.timeLapse).isEqualTo(event.timeLapse)
    Truth.assertThat(vm.timeLapseUnit).isEqualTo(event.timeLapseUnit)
    Truth.assertThat(vm.progressDate).isEqualTo(event.progressDate)
  }

  private fun assertEvent(event: Event, vm: EventViewModel) {
    Truth.assertThat(event.id).isEqualTo(vm.id)
    Truth.assertThat(event.name).isEqualTo(vm.name)
    Truth.assertThat(event.description).isEqualTo(vm.description)
    Truth.assertThat(event.date).isEqualTo(vm.date)
    Truth.assertThat(event.tags).isEqualTo(vm.tags)
    Truth.assertThat(event.favorite).isEqualTo(vm.favorite)
    Truth.assertThat(event.reminder).isEqualTo(vm.reminder)
    Truth.assertThat(event.reminderUnit).isEqualTo(vm.reminderUnit)
    Truth.assertThat(event.timeLapse).isEqualTo(vm.timeLapse)
    Truth.assertThat(event.timeLapseUnit).isEqualTo(vm.timeLapseUnit)
    Truth.assertThat(event.progressDate).isEqualTo(vm.progressDate)
  }

}