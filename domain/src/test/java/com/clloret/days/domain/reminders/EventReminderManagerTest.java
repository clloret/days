package com.clloret.days.domain.reminders;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.common.SimpleDateConverter;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.events.EventPeriodFormat;
import com.clloret.days.domain.utils.Optional;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(JUnitParamsRunner.class)
public class EventReminderManagerTest {

  private static final String TEST_EVENT_ID = "Id";
  private static final String TEST_EVENT_NAME = "Sample event";
  private static final String TEST_EVENT_DESCRIPTION = "This is a sample event";
  private static final String STUB_NOTIFICATION_BIG_TEXT = "<b>%s</b><br/>%s";
  private static final String STUB_TIME_LAPSE_FORMATTED = "1 day ago";
  private static final String EXPECTED_NOTIFICATION_BIG_TEXT = "<b>1 day ago</b><br/>This is a sample event";
  private LocalDate today;
  private LocalDate tomorrow;
  private DateTime now;

  @Mock
  private ReminderManager reminderManager;

  @Mock
  private TimeProvider timeProvider;

  @Mock
  private PreferenceUtils preferenceUtils;

  @SuppressWarnings("unused")
  @Mock
  private StringResourceProvider stringResourceProvider;

  @Mock
  private EventPeriodFormat eventPeriodFormat;

  @InjectMocks
  private EventReminderManager sut;

  private Event getTestEvent(Date eventDate, Integer reminder, TimeUnit reminderUnit) {

    return new EventBuilder()
        .setId(TEST_EVENT_ID)
        .setName(TEST_EVENT_NAME)
        .setDescription(TEST_EVENT_DESCRIPTION)
        .setDate(eventDate)
        .setReminderUnit(reminderUnit)
        .setReminder(reminder)
        .build();
  }

  private void addReminderManagerStubs() {

    doNothing()
        .when(reminderManager)
        .addReminder(isA(Event.class), isA(String.class), isA(Date.class), isA(String.class),
            isA(String.class), any());

    doNothing()
        .when(reminderManager)
        .removeReminderForEvent(isA(Event.class));

    when(timeProvider.getCurrentTime())
        .thenReturn(now);

    when(preferenceUtils.getReminderTime())
        .thenReturn(0);

    when(stringResourceProvider.getNotificationBigText())
        .thenReturn(STUB_NOTIFICATION_BIG_TEXT);

    when(eventPeriodFormat.getTimeLapseFormatted(isA(Date.class), isA(Date.class)))
        .thenReturn(STUB_TIME_LAPSE_FORMATTED);
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

  @SuppressWarnings("unchecked")
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

    ArgumentCaptor<Optional> optionalCaptor = ArgumentCaptor.forClass(Optional.class);

    verify(reminderManager).removeReminderForEvent(any());
    verify(reminderManager)
        .addReminder(any(), anyString(), eq(reminderDate),
            eq(TEST_EVENT_NAME),
            eq(STUB_TIME_LAPSE_FORMATTED),
            optionalCaptor.capture());

    Optional<String> result = optionalCaptor.getValue();
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get()).isEqualTo(EXPECTED_NOTIFICATION_BIG_TEXT);
  }

  @Test
  public void scheduleReminder_WhenNoRemovePreviously_ScheduledReminderIsNotRemoved() {

    addReminderManagerStubs();

    Event event = getTestEvent(today.toDate(), 1, TimeUnit.DAY);

    sut.scheduleReminder(event, false);

    verify(reminderManager, never()).removeReminderForEvent(any());
    verify(reminderManager)
        .addReminder(any(), anyString(), eq(tomorrow.toDate()), anyString(),
            anyString(), any());
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
    verify(reminderManager, never())
        .addReminder(any(), anyString(), any(), anyString(), anyString(), any());
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
    verify(reminderManager, never())
        .addReminder(any(), anyString(), any(), anyString(), anyString(), any());
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
        .addReminder(any(), anyString(), eq(tomorrow.toDate()), anyString(),
            anyString(), any());
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
        .addReminder(any(), anyString(), eq(tomorrow.toDate()), anyString(),
            anyString(), any());
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