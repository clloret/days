package com.clloret.days.domain.reminders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.common.SimpleDateConverter;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.TimeProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.Param;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnitParamsRunner.class)
public class EventReminderManagerTest {

  private LocalDate today;
  private LocalDate tomorrow;
  private DateTime now;

  @Mock
  private ReminderManager reminderManager;

  @Mock
  private TimeProvider timeProvider;

  @Mock
  private PreferenceUtils preferenceUtils;

  @InjectMocks
  private EventReminderManager sut;

  private Event getTestEvent(Date eventDate, Integer reminder, TimeUnit reminderUnit) {

    return new EventBuilder()
        .setId("Id")
        .setName("Sample event")
        .setDate(eventDate)
        .setReminderUnit(reminderUnit)
        .setReminder(reminder)
        .build();
  }

  private void addReminderManagerStubs() {

    doNothing()
        .when(reminderManager)
        .addReminder(ArgumentMatchers.isA(Event.class), ArgumentMatchers.isA(String.class),
            ArgumentMatchers.isA(String.class), ArgumentMatchers.isA(Date.class));

    doNothing()
        .when(reminderManager)
        .removeReminderForEvent(ArgumentMatchers.isA(Event.class));

    when(timeProvider.getCurrentTime())
        .thenReturn(now);

    when(preferenceUtils.getReminderTime())
        .thenReturn(0);
  }

  @Before
  public void setUp() {

    today = new LocalDate(2000, 1, 1);
    tomorrow = new LocalDate(2000, 1, 2);
    now = new DateTime()
        .withDate(today)
        .withTime(0, 0, 0, 0);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  @Parameters({
      "01.01.2000, 1, DAY, 2.01.2000",
      "01.12.1999, 2, MONTH, 01.02.2000",
      "01.01.1999, 2, YEAR, 01.01.2001"
  })
  public void scheduleReminder_Always_ScheduleReminder(
      @Param(converter = SimpleDateConverter.class) Date eventDate, int reminder,
      TimeUnit reminderUnit, @Param(converter = SimpleDateConverter.class) Date reminderDate) {

    addReminderManagerStubs();

    Event event = getTestEvent(eventDate, reminder, reminderUnit);

    sut.scheduleReminder(event, true);

    verify(reminderManager).removeReminderForEvent(any());
    verify(reminderManager)
        .addReminder(any(), anyString(), anyString(), ArgumentMatchers.eq(reminderDate));
  }

  @Test
  public void scheduleReminder_WhenNoRemovePreviously_ScheduledReminderIsNotRemoved() {

    addReminderManagerStubs();

    Event event = getTestEvent(today.toDate(), 1, TimeUnit.DAY);

    sut.scheduleReminder(event, false);

    verify(reminderManager, never()).removeReminderForEvent(any());
    verify(reminderManager)
        .addReminder(any(), anyString(), anyString(), ArgumentMatchers.eq(tomorrow.toDate()));
  }

  @Test
  @Parameters({
      "01.01.2000, -1, DAY",
      "01.12.1999, -2, MONTH",
      "01.01.1999, -2, YEAR"
  })
  public void scheduleReminder_WhenReminderTimeIsPast_ReminderIsNotScheduled(
      @Param(converter = SimpleDateConverter.class) Date eventDate, int reminder,
      TimeUnit reminderUnit) {

    addReminderManagerStubs();

    Event event = getTestEvent(eventDate, reminder, reminderUnit);

    sut.scheduleReminder(event, true);

    verify(reminderManager).removeReminderForEvent(any());
    verify(reminderManager, never()).addReminder(any(), anyString(), anyString(), any());
  }

  @Test
  @Parameters({
      "01.01.2000, DAY",
      "01.12.1999, MONTH",
      "01.01.1999, YEAR"
  })
  public void scheduleReminder_WhenNoReminder_ReminderIsNotScheduled(
      @Param(converter = SimpleDateConverter.class) Date eventDate, TimeUnit reminderUnit) {

    addReminderManagerStubs();

    Event event = getTestEvent(eventDate, null, reminderUnit);

    sut.scheduleReminder(event, true);

    verify(reminderManager).removeReminderForEvent(any());
    verify(reminderManager, never()).addReminder(any(), anyString(), anyString(), any());
  }

  @Test
  public void scheduleReminders_Always_ScheduleReminders() {

    addReminderManagerStubs();

    Event event = getTestEvent(today.toDate(), 1, TimeUnit.DAY);

    List<Event> events = new ArrayList<>();
    events.add(event);
    events.add(event);

    sut.scheduleReminders(events, true);

    int wantedNumberOfInvocations = events.size();
    verify(reminderManager, times(wantedNumberOfInvocations)).removeReminderForEvent(any());
    verify(reminderManager, times(wantedNumberOfInvocations))
        .addReminder(any(), anyString(), anyString(), ArgumentMatchers.eq(tomorrow.toDate()));
  }

  @Test
  public void scheduleReminders_WhenNoRemovePreviously_ScheduledReminderIsNotRemoved() {

    addReminderManagerStubs();

    Event event = getTestEvent(today.toDate(), 1, TimeUnit.DAY);

    List<Event> events = new ArrayList<>();
    events.add(event);
    events.add(event);

    sut.scheduleReminders(events, false);

    int wantedNumberOfInvocations = events.size();
    verify(reminderManager, never()).removeReminderForEvent(any());
    verify(reminderManager, times(wantedNumberOfInvocations))
        .addReminder(any(), anyString(), anyString(), ArgumentMatchers.eq(tomorrow.toDate()));
  }

  @Test
  @Parameters({
      "01.01.2000, 1, DAY",
      "01.12.1999, 2, MONTH",
      "01.01.1999, 2, YEAR"
  })
  public void removeReminder_Always_RemoveScheduledReminder(
      @Param(converter = SimpleDateConverter.class) Date eventDate, int reminder,
      TimeUnit reminderUnit) {

    addReminderManagerStubs();

    Event event = getTestEvent(eventDate, reminder, reminderUnit);

    sut.removeReminder(event);

    verify(reminderManager).removeReminderForEvent(any());
  }
}