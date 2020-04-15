package com.clloret.days.domain.events.order

import java.util.*

interface EventSortable {
  val isFavorite: Boolean
  val name: String?
  val date: Date
}