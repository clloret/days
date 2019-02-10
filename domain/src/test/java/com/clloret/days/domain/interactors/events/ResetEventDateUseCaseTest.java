package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.reminders.EventRemindersManager;
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

public class ResetEventDateUseCaseTest {

  private LocalDate today;

  @Mock
  private AppDataStore dataStore;

  @Mock
  private EventRemindersManager eventRemindersManager;

  @Mock
  private TimeProvider timeProvider;

  @InjectMocks
  private ResetEventDateUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addGetCurrentDateStubToTimeProvider(timeProvider, today);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventRemindersManager);
  }

  private void verifyDataStoreMockInteractions(Event event) {

    Mockito.verify(dataStore, Mockito.times(1))
        .editEvent(event);
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
    CommonMocksInteractions
        .verifyEventRemindersManagerMockInteractions(resetEvent, eventRemindersManager,
            true, true);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> Objects.equals(event.getDate(), today.toDate()));
  }
}