package com.clloret.days.domain.interactors.events;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.RxImmediateThreadingSchedulers;
import com.clloret.days.domain.utils.ThreadSchedulers;
import com.clloret.days.domain.utils.TimeProvider;
import io.reactivex.observers.TestObserver;
import java.util.Date;
import java.util.Objects;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ResetEventDateUseCaseTest {

  private LocalDate today;

  @Mock
  private EventRepository dataStore;

  @Mock
  private EventReminderManager eventReminderManager;

  @Mock
  private TimeProvider timeProvider;

  @Spy
  private ThreadSchedulers threadSchedulers = new RxImmediateThreadingSchedulers();

  @InjectMocks
  private ResetEventDateUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addGetCurrentDateStubToTimeProvider(timeProvider, today);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventReminderManager);
  }

  private void verifyDataStoreMockInteractions(Event event) {

    Mockito.verify(dataStore, Mockito.times(1))
        .edit(event);
    Mockito.verifyNoMoreInteractions(dataStore);
  }

  @Before
  public void setUp() {

    today = new LocalDate()
        .withDayOfMonth(1)
        .withMonthOfYear(1)
        .withYear(2000);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void execute_WhenChangeReminder_AddScheduleReminder_AndRemovePreviously() {

    Date date = new LocalDate()
        .withDayOfMonth(1)
        .withMonthOfYear(1)
        .withYear(2018)
        .toDate();

    Event resetEvent = new EventBuilder()
        .setDate(date)
        .setReminder(7)
        .setReminderUnit(TimeUnit.DAY)
        .build();

    addMethodStubs(resetEvent);

    TestObserver<Event> testObserver = sut
        .execute(resetEvent)
        .test();

    verifyDataStoreMockInteractions(resetEvent);
    verify(eventReminderManager, Mockito.times(1))
        .scheduleReminder(resetEvent, true);
    verifyNoMoreInteractions(eventReminderManager);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> Objects.equals(event.getDate(), today.toDate()));
  }
}