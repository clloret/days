package com.clloret.days.model.entities.mapper

import com.clloret.days.domain.entities.Event
import com.clloret.days.model.entities.EventViewModel
import java.util.*

fun Event.toEventViewModel() = EventViewModel(
        id = id,
        name = name,
        description = description,
        date = date,
        tags = tags,
        favorite = favorite,
        reminder = reminder,
        reminderUnit = reminderUnit,
        timeLapse = timeLapse,
        timeLapseUnit = timeLapseUnit,
        progressDate = progressDate
)

fun EventViewModel.toEvent() = Event(
        id = id,
        name = name,
        description = description,
        date = date,
        tags = tags,
        favorite = favorite,
        reminder = reminder,
        reminderUnit = reminderUnit,
        timeLapse = timeLapse,
        timeLapseUnit = timeLapseUnit,
        progressDate = progressDate
)

fun List<Event>.toEventViewModelList(): List<EventViewModel> {
  val mappedList: MutableList<EventViewModel> = ArrayList(20)
  for (event in this) {
    val mappedEntity: EventViewModel = event.toEventViewModel()
    mappedList.add(mappedEntity)
  }
  return mappedList
}
