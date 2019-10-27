package com.clloret.days.domain.events.order;

import static com.google.common.truth.Truth.assertThat;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.events.order.EventSortFactory.SortType;
import java.util.Comparator;
import java.util.Date;
import org.joda.time.LocalDate;
import org.junit.Test;

public class EventSortFactoryTest {

  private Date oldestDate = new LocalDate()
      .withDayOfMonth(1)
      .withMonthOfYear(1)
      .withYear(2018)
      .toDate();

  private Date latestDate = new LocalDate()
      .withDayOfMonth(2)
      .withMonthOfYear(2)
      .withYear(2019)
      .toDate();

  private Event event1 = new EventBuilder()
      .setName("A")
      .setFavorite(true)
      .setDate(oldestDate)
      .build();

  private Event event2 = new EventBuilder()
      .setName("Z")
      .setFavorite(false)
      .setDate(latestDate)
      .build();

  @Test
  public void makeEventSort_WhenSortTypeName_ReturnCorrectComparator() {

    Comparator<EventSortable> sut = EventSortFactory.makeEventSort(SortType.NAME);

    int result = sut.compare(event1, event2);

    assertThat(result).isLessThan(0);
  }

  @Test
  public void makeEventSort_WhenSortTypeFavorite_ReturnCorrectComparator() {

    Comparator<EventSortable> sut = EventSortFactory.makeEventSort(SortType.FAVORITE);

    int result = sut.compare(event1, event2);

    assertThat(result).isLessThan(0);
  }

  @Test
  public void makeEventSort_WhenSortTypeLatestDate_ReturnCorrectComparator() {

    Comparator<EventSortable> sut = EventSortFactory.makeEventSort(SortType.LATEST_DATE);

    int result = sut.compare(event2, event1);

    assertThat(result).isLessThan(0);
  }

  @Test
  public void makeEventSort_WhenSortTypeOldestDate_ReturnCorrectComparator() {

    Comparator<EventSortable> sut = EventSortFactory.makeEventSort(SortType.OLDEST_DATE);

    int result = sut.compare(event1, event2);

    assertThat(result).isLessThan(0);
  }

}