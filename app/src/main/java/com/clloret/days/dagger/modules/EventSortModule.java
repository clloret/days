package com.clloret.days.dagger.modules;

import com.clloret.days.domain.events.order.EventSortFactory;
import com.clloret.days.domain.events.order.EventSortFactory.SortType;
import com.clloret.days.domain.events.order.EventSortable;
import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import java.util.Comparator;
import javax.inject.Singleton;

@Module
public class EventSortModule {

  @MapKey
  @interface SortTypeEnumKey {

    SortType value();
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(SortType.NAME)
  Comparator<EventSortable> providesEventSortByName() {

    return EventSortFactory.makeEventSort(SortType.NAME);
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(SortType.FAVORITE)
  Comparator<EventSortable> providesEventSortByFavorite() {

    return EventSortFactory.makeEventSort(SortType.FAVORITE);
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(SortType.LATEST_DATE)
  Comparator<EventSortable> providesEventSortByLatestDate() {

    return EventSortFactory.makeEventSort(SortType.LATEST_DATE);
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(SortType.OLDEST_DATE)
  Comparator<EventSortable> providesEventSortByOldestDate() {

    return EventSortFactory.makeEventSort(SortType.OLDEST_DATE);
  }

}
