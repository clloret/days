package com.clloret.days.domain.timelapse;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.common.SimpleDateConverter;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.RxImmediateSchedulerRule;
import com.clloret.days.domain.utils.TimeProvider;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Param;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnitParamsRunner.class)
public class TimeLapseManagerTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  private LocalDate today;
  private DateTime now;

  @Mock
  private TimeProvider timeProvider;

  @Mock
  private EventReminderManager eventReminderManager;

  @Mock
  private EventRepository appDataStore;

  @InjectMocks
  private TimeLapseManager sut;

  private Event getTestEvent(Date eventDate, Integer timeLapse, TimeUnit timeLapseUnit) {

    return new EventBuilder()
        .setId("Id")
        .setName("Sample event")
        .setDate(eventDate)
        .setTimeLapse(timeLapse)
        .setTimeLapseUnit(timeLapseUnit)
        .build();
  }

  private void addStubs() {

    when(timeProvider.getCurrentDate())
        .thenReturn(today);

    when(timeProvider.getCurrentTime())
        .thenReturn(now);

    doNothing()
        .when(eventReminderManager)
        .scheduleReminder(ArgumentMatchers.isA(Event.class), anyBoolean());

    when(appDataStore.getAll(true))
        .thenReturn(Single.just(createEventList()));

    when(appDataStore.edit(ArgumentMatchers.isA(Event.class)))
        .thenReturn(Maybe.just(new Event()));
  }

  @Before
  public void setUp() {

    today = new LocalDate(2000, 1, 1);
    now = new DateTime()
        .withDate(today)
        .withTime(0, 0, 0, 0);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  @Parameters({
      "01.01.2000, 7, DAY, 8.01.2000",
      "01.12.1999, 2, MONTH, 01.02.2000",
      "01.01.1999, 2, YEAR, 01.01.2001"
  })
  public void updateEventDateFromTimeLapse_Always_ScheduleReminder(
      @Param(converter = SimpleDateConverter.class) Date eventDate, int timeLapse,
      TimeUnit timeLapseUnit, @Param(converter = SimpleDateConverter.class) Date newDate) {

    addStubs();

    Event event = getTestEvent(eventDate, timeLapse, timeLapseUnit);

    Event result = sut.updateEventDateFromTimeLapse(event);

    assertThat(result.getDate(), is(newDate));
  }

  @Test
  public void updateEventAndSave_Always_UpdateEventAndScheduleReminder() {

    addStubs();

    TestObserver<Event> testObserver = sut.updateEventsDatesFromTimeLapse().test();

    testObserver.awaitTerminalEvent();

    testObserver
        .assertComplete()
        .assertNoErrors();

    verify(appDataStore)
        .getAll(true);

    int wantedNumberOfInvocations = 2;

    verify(appDataStore, times(wantedNumberOfInvocations))
        .edit(ArgumentMatchers.isA(Event.class));
    verify(eventReminderManager, times(wantedNumberOfInvocations))
        .scheduleReminder(ArgumentMatchers.isA(Event.class), anyBoolean());
  }

  public List<Event> createEventList() {

    Event[] events = {
        new EventBuilder()
            .setDate(today.toDate())
            .setTimeLapse(7)
            .setTimeLapseUnit(TimeUnit.DAY)
            .build(),
        new EventBuilder()
            .setDate(today.plusDays(1).toDate())
            .setTimeLapse(7)
            .setTimeLapseUnit(TimeUnit.DAY)
            .build(),
        new EventBuilder()
            .setDate(today.minusDays(1).toDate())
            .setTimeLapse(7)
            .setTimeLapseUnit(TimeUnit.DAY)
            .build(),
        new EventBuilder()
            .setDate(today.toDate())
            .build()};

    return Arrays.asList(events);
  }

}