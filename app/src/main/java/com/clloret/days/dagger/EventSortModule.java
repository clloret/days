package com.clloret.days.dagger;

import com.clloret.days.events.list.order.EventSortFactory;
import com.clloret.days.events.list.order.EventSortFactory.SortType;
import com.clloret.days.model.entities.EventViewModel;
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
  public static Comparator<EventViewModel> providesEventSortByName() {

    return EventSortFactory.makeEventSort(SortType.NAME);
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(SortType.FAVORITE)
  public static Comparator<EventViewModel> providesEventSortByFavorite() {

    return EventSortFactory.makeEventSort(SortType.FAVORITE);
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(SortType.LATEST_DATE)
  public static Comparator<EventViewModel> providesEventSortByLatestDate() {

    return EventSortFactory.makeEventSort(SortType.LATEST_DATE);
  }

  @Provides
  @Singleton
  @IntoMap
  @SortTypeEnumKey(SortType.OLDEST_DATE)
  public static Comparator<EventViewModel> providesEventSortByOldestDate() {

    return EventSortFactory.makeEventSort(SortType.OLDEST_DATE);
  }

}
