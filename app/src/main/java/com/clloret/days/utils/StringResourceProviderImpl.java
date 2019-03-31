package com.clloret.days.utils;

import android.content.res.Resources;
import com.clloret.days.R;
import com.clloret.days.domain.utils.StringResourceProvider;

public class StringResourceProviderImpl implements StringResourceProvider {

  private final Resources resources;

  public StringResourceProviderImpl(Resources resources) {

    this.resources = resources;
  }

  @Override
  public String getEventResetNotificationAction() {

    return resources.getString(R.string.notification_action_event_reset);
  }

  @Override
  public String getEventDeleteNotificationAction() {

    return resources.getString(R.string.notification_action_event_delete);
  }

  @Override
  public String getPrefReminderTimeKey() {

    return resources.getString(R.string.pref_reminder_time);
  }
}
