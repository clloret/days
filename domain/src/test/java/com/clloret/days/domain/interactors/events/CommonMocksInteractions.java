package com.clloret.days.domain.interactors.events;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.reminders.EventRemindersManager;
import com.clloret.days.domain.utils.TimeProvider;
import io.reactivex.Maybe;
import org.joda.time.LocalDate;
import org.mockito.Mockito;

class CommonMocksInteractions {

  static void addDataStoreStubs(AppDataStore dataStore, Event event) {

    Mockito.when(dataStore.createEvent(event))
        .thenReturn(Maybe.just(event));

    Mockito.when(dataStore.editEvent(event))
        .thenReturn(Maybe.just(event));

    when(dataStore.deleteEvent(event))
        .thenReturn(Maybe.just(true));
  }

  static void addScheduleReminderStubToEventRemindersManager(
      EventRemindersManager eventRemindersManager) {

    doNothing().when(eventRemindersManager)
        .scheduleReminder(isA(Event.class), isA(Boolean.class), isA(Boolean.class));
  }

  static void addGetCurrentDateStubToTimeProvider(TimeProvider timeProvider,
      LocalDate today) {

    when(timeProvider.getCurrentDate())
        .thenReturn(today);
  }

  static void verifyEventRemindersManagerMockInteractions(Event event,
      EventRemindersManager eventRemindersManager,
      boolean add,
      boolean removePreviously) {

    Mockito.verify(eventRemindersManager, Mockito.times(1))
        .scheduleReminder(event, add, removePreviously);
    Mockito.verifyNoMoreInteractions(eventRemindersManager);
  }
}
