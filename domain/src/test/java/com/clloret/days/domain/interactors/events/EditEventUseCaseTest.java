package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.interactors.events.EditEventUseCase.RequestValues;
import com.clloret.days.domain.reminders.EventRemindersManager;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EditEventUseCaseTest {

  @Mock
  private AppDataStore dataStore;

  @Mock
  private EventRemindersManager eventRemindersManager;

  @InjectMocks
  private EditEventUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
    CommonMocksInteractions.addScheduleReminderStubToEventRemindersManager(eventRemindersManager);
  }

  private void verifyDataStoreMockInteractions(Event event) {

    Mockito.verify(dataStore, Mockito.times(1))
        .editEvent(event);
    Mockito.verifyNoMoreInteractions(dataStore);
  }

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void execute_WhenEnableReminder_AddScheduleReminder() {

    Event modifiedEvent = new EventBuilder()
        .setReminder(7)
        .setReminderUnit(TimeUnit.DAY)
        .build();

    Event originalEvent = new EventBuilder()
        .build();

    addMethodStubs(modifiedEvent);

    RequestValues requestValues = new RequestValues(modifiedEvent, originalEvent);
    TestObserver<Event> testObserver = sut
        .execute(requestValues)
        .test();

    verifyDataStoreMockInteractions(modifiedEvent);
    CommonMocksInteractions
        .verifyEventRemindersManagerMockInteractions(modifiedEvent, eventRemindersManager, true,
            false);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> event.getReminder() == 7);
  }

  @Test
  public void execute_WhenDisableReminder_RemoveScheduleReminder() {

    Event modifiedEvent = new EventBuilder()
        .setReminder(null)
        .build();

    Event originalEvent = new EventBuilder()
        .setReminder(7)
        .setReminderUnit(TimeUnit.DAY)
        .build();

    addMethodStubs(modifiedEvent);

    RequestValues requestValues = new RequestValues(modifiedEvent, originalEvent);
    TestObserver<Event> testObserver = sut
        .execute(requestValues)
        .test();

    verifyDataStoreMockInteractions(modifiedEvent);
    CommonMocksInteractions
        .verifyEventRemindersManagerMockInteractions(modifiedEvent, eventRemindersManager, false,
            true);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> event.getReminder() == null);
  }

  @Test
  public void execute_WhenChangeReminder_AddScheduleReminder_AndRemovePreviously() {

    Event modifiedEvent = new EventBuilder()
        .setReminder(1)
        .setReminderUnit(TimeUnit.MONTH)
        .build();

    Event originalEvent = new EventBuilder()
        .setReminder(7)
        .setReminderUnit(TimeUnit.DAY)
        .build();

    addMethodStubs(modifiedEvent);

    RequestValues requestValues = new RequestValues(modifiedEvent, originalEvent);
    TestObserver<Event> testObserver = sut
        .execute(requestValues)
        .test();

    verifyDataStoreMockInteractions(modifiedEvent);
    CommonMocksInteractions
        .verifyEventRemindersManagerMockInteractions(modifiedEvent, eventRemindersManager, true,
            true);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> event.getReminder() == 1)
        .assertValue(event -> event.getReminderUnit() == TimeUnit.MONTH);
  }
}