package com.clloret.days.dagger.modules

import com.clloret.days.domain.events.order.EventSortFactory
import com.clloret.days.domain.events.order.EventSortable
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import java.util.*
import javax.inject.Singleton

@Module
class EventSortModule {

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(EventSortFactory.SortType.NAME)
  fun providesEventSortByName(): Comparator<EventSortable> {
    return EventSortFactory.makeEventSort(EventSortFactory.SortType.NAME)
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(EventSortFactory.SortType.FAVORITE)
  fun providesEventSortByFavorite(): Comparator<EventSortable> {
    return EventSortFactory.makeEventSort(EventSortFactory.SortType.FAVORITE)
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(EventSortFactory.SortType.LATEST_DATE)
  fun providesEventSortByLatestDate(): Comparator<EventSortable> {
    return EventSortFactory.makeEventSort(EventSortFactory.SortType.LATEST_DATE)
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(EventSortFactory.SortType.OLDEST_DATE)
  fun providesEventSortByOldestDate(): Comparator<EventSortable> {
    return EventSortFactory.makeEventSort(EventSortFactory.SortType.OLDEST_DATE)
  }

  @MapKey
  internal annotation class SortTypeEnumKey(val value: EventSortFactory.SortType)

}