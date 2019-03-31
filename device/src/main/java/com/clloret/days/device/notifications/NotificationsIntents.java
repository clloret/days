package com.clloret.days.device.notifications;

import android.app.PendingIntent;
import com.clloret.days.domain.entities.Event;

public interface NotificationsIntents {

  PendingIntent getViewEventIntent(Event event);

  PendingIntent getDeleteEventIntent(Event event);

  PendingIntent getResetEventIntent(Event event);
}
