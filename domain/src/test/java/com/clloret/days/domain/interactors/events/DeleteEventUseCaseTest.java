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

public class DeleteEventUseCaseTest {

  @Mock
  private AppDataStore dataStore;

  @Mock
  private EventRemindersManager eventRemindersManager;

  @InjectMocks
  private DeleteEventUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventRemindersManager);
  }

  private void verifyMockInteractions(Event event) {

    Mockito.verify(dataStore, Mockito.times(1))
        .deleteEvent(event);
    Mockito.verifyNoMoreInteractions(dataStore);
  }

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void execute_WhenDeleteEventWithReminder_RemoveScheduleReminder() {

    Event deletedEvent = new EventBuilder()
        .setReminder(7)
        .setReminderUnit(TimeUnit.DAY)
        .build();

    addMethodStubs(deletedEvent);

    TestObserver<Boolean> testObserver = sut
        .execute(deletedEvent)
        .test();

    verifyMockInteractions(deletedEvent);
    CommonMocksInteractions
        .verifyEventRemindersManagerMockInteractions(deletedEvent, eventRemindersManager, false,
            true);

    testObserver
        .assertComplete()
        .assertNoErrors();
  }
}