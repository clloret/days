package com.clloret.days.domain.interactors.events;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DeleteEventUseCaseTest {

  @Mock
  private EventRepository dataStore;

  @Mock
  private EventReminderManager eventReminderManager;

  @InjectMocks
  private DeleteEventUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventReminderManager);
  }

  private void verifyMockInteractions(Event event) {

    Mockito.verify(dataStore, Mockito.times(1))
        .delete(event);
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
    verify(eventReminderManager, Mockito.times(1))
        .removeReminder(deletedEvent);
    verifyNoMoreInteractions(eventReminderManager);

    testObserver
        .assertComplete()
        .assertNoErrors();
  }
}