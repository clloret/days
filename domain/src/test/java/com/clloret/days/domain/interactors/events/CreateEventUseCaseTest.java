package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.reminders.EventRemindersManager;
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
  private EventRemindersManager eventRemindersManager;

  @InjectMocks
  private CreateEventUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventRemindersManager);
  }

  private void verifyDataStoreMockInteractions(Event newEvent) {

    Mockito.verify(dataStore, Mockito.times(1))
        .createEvent(newEvent);
    Mockito.verifyNoMoreInteractions(dataStore);
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
    CommonMocksInteractions
        .verifyEventRemindersManagerMockInteractions(newEvent, eventRemindersManager, true,
            false);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> event.getReminder() == 7);
  }
}