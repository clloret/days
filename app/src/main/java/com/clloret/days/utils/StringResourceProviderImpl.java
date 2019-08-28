package com.clloret.days.utils;

import android.content.res.Resources;
import com.clloret.days.R;
import com.clloret.days.domain.utils.StringResourceProvider;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StringResourceProviderImpl implements StringResourceProvider {

  private final Resources resources;

  @SuppressWarnings("WeakerAccess")
  @Inject
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

  @Override
  public String getPeriodFormatBefore() {

    return resources.getString(R.string.notification_period_format_before);
  }

  @Override
  public String getPeriodFormatToday() {

    return resources.getString(R.string.notification_period_format_today);
  }

  @Override
  public String getPeriodFormatAfter() {

    return resources.getString(R.string.notification_period_format_after);
  }

  @Override
  public String getNotificationBigText() {

    return resources.getString(R.string.notification_notification_big_text);
  }

  @Override
  public String getPrefDefaultList() {

    return resources.getString(R.string.pref_default_list);
  }

  @Override
  public String getPrefRemoteDatastore() {

    return resources.getString(R.string.pref_remote_datastore);
  }

  @Override
  public String getPrefAirtableApiKey() {

    return resources.getString(R.string.pref_airtable_api_key);
  }

  @Override
  public String getPrefAirtableBaseId() {

    return resources.getString(R.string.pref_airtable_base_id);
  }

  @Override
  public String getPrefSortMode() {

    return resources.getString(R.string.pref_sort_mode);
  }
}
