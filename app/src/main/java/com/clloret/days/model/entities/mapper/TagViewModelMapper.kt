package com.clloret.days.model.entities.mapper

import com.clloret.days.domain.entities.Tag
import com.clloret.days.model.entities.TagViewModel
import java.util.*

fun Tag.toTagViewModel() = TagViewModel(
        id = id,
        name = name
)

fun TagViewModel.toTag() = Tag(
        id = id,
        name = name
)

fun List<Tag>.toTagViewModelList(): List<TagViewModel> {
  val mappedList: MutableList<TagViewModel> = ArrayList(20)
  for (event in this) {
    val mappedEntity: TagViewModel = event.toTagViewModel()
    mappedList.add(mappedEntity)
  }
  return mappedList
}
