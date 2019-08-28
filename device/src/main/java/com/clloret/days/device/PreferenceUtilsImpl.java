package com.clloret.days.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.clloret.days.domain.events.order.EventSortFactory.SortType;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceUtilsImpl implements PreferenceUtils {

  private static final int DEFAULT_REMINDER_TIME = 0;
  private static final int DEFAULT_SORT_MODE = SortType.NAME.getValue();
  private static final boolean DEFAULT_USE_REMOTE_DATASTORE = false;
  private static final String DEFAULT_LIST = "0";
  private static final String DEFAULT_AIRTABLE_API_KEY = "";
  private static final String DEFAULT_AIRTABLE_BASE_IDE = "";

  private final StringResourceProvider stringResourceProvider;
  private final SharedPreferences preferences;

  @Inject
  public PreferenceUtilsImpl(
      Context context, StringResourceProvider stringResourceProvider) {

    preferences = PreferenceManager.getDefaultSharedPreferences(context);
    this.stringResourceProvider = stringResourceProvider;
  }

  @Override
  public boolean getUseRemoteDataStore() {

    return preferences
        .getBoolean(stringResourceProvider.getPrefRemoteDatastore(), DEFAULT_USE_REMOTE_DATASTORE);
  }

  @Override
  public int getReminderTime() {

    return preferences
        .getInt(stringResourceProvider.getPrefReminderTimeKey(), DEFAULT_REMINDER_TIME);
  }

  @Override
  public int getSortMode() {

    return preferences
        .getInt(stringResourceProvider.getPrefSortMode(), DEFAULT_SORT_MODE);
  }

  @Override
  public void setSortMode(SortType sortType) {

    preferences.edit().putInt(stringResourceProvider.getPrefSortMode(), sortType.getValue())
        .apply();
  }

  @Override
  public String getAirtableApiKey() {

    return preferences
        .getString(stringResourceProvider.getPrefAirtableApiKey(), DEFAULT_AIRTABLE_API_KEY);
  }

  @Override
  public String getAirtableBaseId() {

    return preferences
        .getString(stringResourceProvider.getPrefAirtableBaseId(), DEFAULT_AIRTABLE_BASE_IDE);
  }

  @Override
  public String getDefaultList() {

    return preferences
        .getString(stringResourceProvider.getPrefDefaultList(), DEFAULT_LIST);
  }
}
