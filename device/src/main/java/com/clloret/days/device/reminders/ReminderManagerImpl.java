package com.clloret.days.device.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import androidx.core.app.NotificationCompat.Action;
import com.clloret.days.device.notifications.NotificationsFactory;
import com.clloret.days.device.notifications.NotificationsIntents;
import com.clloret.days.device.notifications.NotificationsUtils;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.reminders.ReminderManager;
import com.clloret.days.domain.utils.StringResourceProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ReminderManagerImpl implements ReminderManager {

  private final ReminderUtils reminderUtils;
  private final NotificationsIntents notificationsIntents;
  private final NotificationsFactory notificationsFactory;
  private final StringResourceProvider stringResourceProvider;

  @Inject
  public ReminderManagerImpl(Context context, NotificationsIntents notificationsIntents,
      StringResourceProvider stringResourceProvider) {

    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationsUtils notificationsUtils = new NotificationsUtils(
        Objects.requireNonNull(notificationManager));

    notificationsFactory = new NotificationsFactory(context,
        context.getResources(),
        notificationsUtils);

    this.notificationsIntents = notificationsIntents;
    this.reminderUtils = new ReminderUtilsImpl(context);
    this.stringResourceProvider = stringResourceProvider;
  }

  @Override
  public void addReminder(Event event, String id, String message, Date date) {

    PendingIntent viewEventIntent = notificationsIntents.getViewEventIntent(event);
    PendingIntent deleteEventIntent = notificationsIntents.getDeleteEventIntent(event);
    PendingIntent resetEventIntent = notificationsIntents.getResetEventIntent(event);

    List<Action> actions = new ArrayList<>();
    actions.add(
        new Action.Builder(0,
            stringResourceProvider.getEventDeleteNotificationAction(),
            deleteEventIntent).build());
    actions.add(
        new Action.Builder(0,
            stringResourceProvider.getEventResetNotificationAction(),
            resetEventIntent).build());

    Notification notification = notificationsFactory
        .createEventReminderNotification(viewEventIntent, message, actions);

    reminderUtils.addReminder(notification, id, message, date);
  }

  @Override
  public void removeReminderForEvent(Event event) {

    reminderUtils.removeReminder(event.getId());
  }

}
