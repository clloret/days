package com.clloret.days.device.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.util.Objects;

class AlarmUtils {

  private AlarmUtils() {

  }

  static void addAlarm(Context context, Intent intent, long timeInMillis) {

    AlarmManager alarmManager = Objects
        .requireNonNull((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
    PendingIntent pendingIntent = PendingIntent
        .getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis,
          pendingIntent);
    } else {
      alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }
  }

  static void cancelAlarm(Context context, Intent intent) {

    AlarmManager alarmManager = Objects
        .requireNonNull((AlarmManager) context.getSystemService(Context.ALARM_SERVICE));
    PendingIntent pendingIntent = PendingIntent
        .getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    alarmManager.cancel(pendingIntent);
    pendingIntent.cancel();
  }

  static boolean hasAlarm(Context context, Intent intent) {

    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE)
        != null;
  }
}
