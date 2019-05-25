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
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ToggleEventReminderUseCaseTest {

  @Mock
  private EventRepository dataStore;

  @Mock
  private EventReminderManager eventReminderManager;

  @Spy
  private ThreadSchedulers threadSchedulers = new RxImmediateThreadingSchedulers();

  @InjectMocks
  private ToggleEventReminderUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventReminderManager);
  }

  private void verifyDataStoreMockInteractions(Event event) {

    Mockito.verify(dataStore, Mockito.times(1))
        .edit(event);
    Mockito.verifyNoMoreInteractions(dataStore);
  }

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void execute_WhenEnableReminder_AddScheduleReminder() {

    Event toggleEvent = new EventBuilder()
        .setReminder(null)
        .build();

    addMethodStubs(toggleEvent);

    TestObserver<Event> testObserver = sut
        .execute(toggleEvent)
        .test();

    verifyDataStoreMockInteractions(toggleEvent);
    verify(eventReminderManager, Mockito.times(1))
        .scheduleReminder(toggleEvent, false);
    verifyNoMoreInteractions(eventReminderManager);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> event.getReminder() == 0);
  }

  @Test
  public void execute_WhenDisableReminder_RemoveScheduleReminder() {

    Event toggleEvent = new EventBuilder()
        .setReminder(7)
        .setReminderUnit(TimeUnit.DAY)
        .build();

    addMethodStubs(toggleEvent);

    TestObserver<Event> testObserver = sut
        .execute(toggleEvent)
        .test();

    verifyDataStoreMockInteractions(toggleEvent);
    verify(eventReminderManager, Mockito.times(1))
        .removeReminder(toggleEvent);
    verifyNoMoreInteractions(eventReminderManager);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> event.getReminder() == null);
  }
}