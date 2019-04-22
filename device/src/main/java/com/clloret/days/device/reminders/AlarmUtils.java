package com.clloret.days.device.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.util.Objects;
import timber.log.Timber;

class AlarmUtils {

  private AlarmUtils() {

  }

  static void addAlarm(Context context, int requestCode, Intent intent, long timeInMillis) {

    Timber.d("addAlarm - requestCode: %d", requestCode);

    AlarmManager alarmManager = Objects
        .requireNonNull((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
    PendingIntent pendingIntent = PendingIntent
        .getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis,
          pendingIntent);
    } else {
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
  }

  static void cancelAlarm(Context context, int requestCode, Intent intent) {

    Timber.d("cancelAlarm - requestCode: %d", requestCode);

    AlarmManager alarmManager = Objects
        .requireNonNull((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
    PendingIntent pendingIntent = PendingIntent
        .getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE);

    if (pendingIntent == null) {
      return;
    }

    alarmManager.cancel(pendingIntent);
    pendingIntent.cancel();
  }

  static boolean hasAlarm(Context context, int requestCode, Intent intent) {

    return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE)
        != null;
  }
}
