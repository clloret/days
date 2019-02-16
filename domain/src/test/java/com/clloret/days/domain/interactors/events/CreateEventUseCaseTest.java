package com.clloret.days.domain.interactors.events;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.reminders.EventReminderManager;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CreateEventUseCaseTest {

  @Mock
  private AppDataStore dataStore;

  @Mock
  private EventReminderManager eventReminderManager;

  @InjectMocks
  private CreateEventUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventReminderManager);
  }

  private void verifyDataStoreMockInteractions(Event newEvent) {

    verify(dataStore, Mockito.times(1))
        .createEvent(newEvent);
    verifyNoMoreInteractions(dataStore);
  }

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void execute_WhenEnableReminder_AddScheduleReminder() {

    Event newEvent = new EventBuilder()
        .setReminder(7)
        .setReminderUnit(TimeUnit.DAY)
        .build();

    addMethodStubs(newEvent);

    TestObserver<Event> testObserver = sut
        .execute(newEvent)
        .test();

    verifyDataStoreMockInteractions(newEvent);
    verify(eventReminderManager, Mockito.times(1))
        .scheduleReminder(newEvent, false);
    verifyNoMoreInteractions(eventReminderManager);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> event.getReminder() == 7);
  }
}