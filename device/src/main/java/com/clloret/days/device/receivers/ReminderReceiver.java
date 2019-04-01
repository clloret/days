package com.clloret.days.device.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.clloret.days.device.notifications.NotificationsUtils;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.ResetEventDateUseCase;
import dagger.android.AndroidInjection;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import timber.log.Timber;

public class ReminderReceiver extends BroadcastReceiver {

  public static final String ACTION_DELETE_REMINDER =
      "com.clloret.days.ACTION_DELETE_REMINDER";

  public static final String ACTION_RESET_EVENT_DATE =
      "com.clloret.days.ACTION_RESET_EVENT_DATE";

  public static final String EXTRA_EVENT_ID = "eventId";

  @Inject
  AppDataStore appDataStore;

  @Inject
  DeleteEventUseCase deleteEventUseCase;

  @Inject
  ResetEventDateUseCase resetEventDateUseCase;

  @Override
  public void onReceive(Context context, Intent intent) {

    Timber.d("onReceive");

    if (context == null || intent == null) {
      return;
    }
    if (intent.getAction() == null) {
      return;
    }

    AndroidInjection.inject(this, context);

    Event event = null;

    if (intent.getExtras() != null) {
      String eventId = intent.getExtras().getString(EXTRA_EVENT_ID);
      event = appDataStore.getEventById(eventId)
          .subscribeOn(Schedulers.io())
          .blockingGet();
    }

    if (event == null) {
      Timber.w("The event id does not exist in the database");
      return;
    }

    switch (intent.getAction()) {
      case ACTION_DELETE_REMINDER:
        cancelEventReminderNotification(context, event);
        deleteEvent(event);
        break;

      case ACTION_RESET_EVENT_DATE:
        cancelEventReminderNotification(context, event);
        resetEventDate(event);
        break;

      default:
        Timber.w("Unknown intent action received");
        break;
    }
  }

  private void resetEventDate(Event event) {

    Timber.d("resetEventDate: %s", event.getName());

    resetEventDateUseCase.execute(event)
        .subscribeOn(Schedulers.io())
        .doOnError(Timber::e)
        .subscribe();
  }

  private void deleteEvent(Event event) {

    Timber.d("deleteEvent: %s", event.getName());

    deleteEventUseCase.execute(event)
        .subscribeOn(Schedulers.io())
        .doOnError(Timber::e)
        .onErrorComplete()
        .subscribe();
  }

  private void cancelEventReminderNotification(Context context, Event event) {

    NotificationManager notificationManager = (NotificationManager) context
        .getSystemService(Context.NOTIFICATION_SERVICE);

    if (notificationManager == null) {
      Timber.w("notificationManager is null");
      return;
    }

    NotificationsUtils notificationsUtils = new NotificationsUtils(notificationManager);
    notificationsUtils.cancelNotification(event.getId());
  }
}
