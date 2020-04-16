package com.clloret.days.domain.entities

import com.clloret.days.domain.tags.order.TagSortable

data class Tag(override var id: String? = null,
               override var name: String) : Identifiable, TagSortable