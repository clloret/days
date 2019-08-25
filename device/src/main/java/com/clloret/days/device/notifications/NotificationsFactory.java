package com.clloret.days.device.notifications;

import static android.os.Build.VERSION.SDK_INT;
import static androidx.core.app.NotificationCompat.CATEGORY_REMINDER;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.Html;
import android.text.Spanned;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.Action;
import androidx.core.app.NotificationCompat.BigTextStyle;
import androidx.core.app.NotificationCompat.Builder;
import com.clloret.days.device.R;
import com.clloret.days.domain.utils.Optional;
import java.util.Date;

public class NotificationsFactory {

  private static final long[] VIBRATION_PATTERN = {100, 200, 300, 400, 500, 400, 300, 200, 400};
  private static final String CHANNEL_REMINDERS_ID = "channel_reminders_id";

  private final Context context;
  private final Resources resources;
  private final NotificationsUtils notificationsUtils;

  public NotificationsFactory(Context context, Resources resources,
      NotificationsUtils notificationsUtils) {

    this.context = context;
    this.resources = resources;
    this.notificationsUtils = notificationsUtils;
  }

  public Notification createEventReminderNotification(PendingIntent contentIntent,
      Date date, String contentTitle, String contentText, Optional<String> bigText,
      Iterable<Action> actions) {

    final Builder builder = getBuilderFromChannel();

    for (Action action : actions) {
      builder.addAction(action);
    }

    builder.setContentTitle(contentTitle)
        .setTicker(contentTitle)
        .setContentText(contentText)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentIntent(contentIntent)
        .setWhen(date.getTime())
        .setShowWhen(true)
        .setAutoCancel(true)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setChannelId(CHANNEL_REMINDERS_ID)
        .setCategory(CATEGORY_REMINDER)
        .setVibrate(VIBRATION_PATTERN);

    bigText.ifPresent(value -> {
      final Spanned htmlBigText = getSpannedHtmlFromText(value);
      builder.setStyle(new BigTextStyle()
          .bigText(htmlBigText));
    });

    return builder.build();
  }

  @SuppressWarnings("deprecation")
  private Spanned getSpannedHtmlFromText(String text) {

    if (VERSION.SDK_INT >= VERSION_CODES.N) {
      return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
    } else {
      return Html.fromHtml(text);
    }
  }

  @NonNull
  private Builder getBuilderFromChannel() {

    Builder builder;

    if (SDK_INT >= VERSION_CODES.O) {
      NotificationChannel channel = notificationsUtils.getNotificationChannel(CHANNEL_REMINDERS_ID);
      if (channel == null) {
        String channelName = resources.getString(R.string.channel_reminders_name);
        notificationsUtils
            .createNotificationChannel(CHANNEL_REMINDERS_ID, channelName, VIBRATION_PATTERN);
      }
      builder = new Builder(context, CHANNEL_REMINDERS_ID);
    } else {

      builder = new Builder(context, CHANNEL_REMINDERS_ID);
      builder.setPriority(Notification.PRIORITY_HIGH);
    }

    return builder;
  }

}
