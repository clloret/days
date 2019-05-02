package com.clloret.days.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceUtilsImpl implements PreferenceUtils {

  private static final int DEFAULT_REMINDER_TIME = 0;

  private final StringResourceProvider stringResourceProvider;
  private final SharedPreferences preferences;

  @Inject
  public PreferenceUtilsImpl(
      Context context, StringResourceProvider stringResourceProvider) {

    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    this.stringResourceProvider = stringResourceProvider;
  }

  @Override
  public int getReminderTime() {

    return preferences
        .getInt(stringResourceProvider.getPrefReminderTimeKey(), DEFAULT_REMINDER_TIME);

  }
}
