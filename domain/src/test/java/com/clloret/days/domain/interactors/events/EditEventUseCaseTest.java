package com.clloret.days.domain.interactors.events;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.events.EventProgressCalculator;
import com.clloret.days.domain.interactors.events.EditEventUseCase.RequestValues;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.RxImmediateThreadingSchedulers;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.observers.TestObserver;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class EditEventUseCaseTest {

  @Mock
  private EventRepository dataStore;

  @Mock
  private EventReminderManager eventReminderManager;

  @Mock
  private EventProgressCalculator eventProgressCalculator;

  @Spy
  private ThreadSchedulers threadSchedulers = new RxImmediateThreadingSchedulers();

  @InjectMocks
  private EditEventUseCase sut;

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
    verify(eventReminderManager, Mockito.times(1))
        .scheduleReminder(modifiedEvent, false);
    verifyNoMoreInteractions(eventReminderManager);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> Objects.requireNonNull(event.getReminder()) == 7);
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
    verify(eventReminderManager, Mockito.times(1))
        .removeReminder(modifiedEvent);
    verifyNoMoreInteractions(eventReminderManager);

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
    verify(eventReminderManager, Mockito.times(1))
        .scheduleReminder(modifiedEvent, true);
    verifyNoMoreInteractions(eventReminderManager);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(event -> Objects.requireNonNull(event.getReminder()) == 1)
        .assertValue(event -> event.getReminderUnit() == TimeUnit.MONTH);
  }
}