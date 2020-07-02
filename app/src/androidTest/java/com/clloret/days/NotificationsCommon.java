package com.clloret.days;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import com.clloret.days.device.reminders.ReminderManagerImpl;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.EventPeriodFormat;
import com.clloret.days.domain.utils.Optional;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.utils.NotificationsIntentsImpl;
import com.clloret.test_android_common.SampleData;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

public class NotificationsCommon {

  public static final long TIMEOUT = 5_000L;

  private final List<Event> sampleEvents = SampleData.getSampleEvents();
  private final StringResourceProvider stringResourceProvider;
  private final EventPeriodFormat eventPeriodFormat;

  public NotificationsCommon(StringResourceProvider stringResourceProvider,
      EventPeriodFormat eventPeriodFormat) {

    this.stringResourceProvider = stringResourceProvider;
    this.eventPeriodFormat = eventPeriodFormat;
  }

  public void showNotification(Event event) {

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    NotificationsIntentsImpl notificationsIntents = new NotificationsIntentsImpl(context);

    ReminderManagerImpl reminderManager = new ReminderManagerImpl(context, notificationsIntents,
        stringResourceProvider);

    String contentTitle = event.getName();
    Date dateReminder = new LocalDate(event.getDate()).minusDays(1).toDate();
    String contentText = eventPeriodFormat
        .getTimeLapseFormatted(event.getDate(), dateReminder, PeriodType.days());
    String notificationBigText = stringResourceProvider.getNotificationBigText();
    String bigText = String.format(notificationBigText, contentText, event.getDescription());
    reminderManager
        .addReminder(event, event.getId(), event.getDate(), contentTitle, contentText,
            Optional.of(bigText));
  }

  @NonNull
  public Event getSampleEventWithTodayDate(int index) {

    Event event = sampleEvents.get(index);
    event.setDate(new Date());
    return event;
  }

}
