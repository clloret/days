package com.clloret.days.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.clloret.days.App;
import com.clloret.days.R;
import com.clloret.days.base.BaseActivity;
import timber.log.Timber;

public class SettingsActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ButterKnife.bind(this);

    configureActionBar(toolbar);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.frame, new CustomPreferenceFragment()).commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_settings, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    switch (id) {

      case R.id.menu_help:
        showHelp();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void showHelp() {

    String url = "https://github.com/clloret/days/wiki";
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

  public static class CustomPreferenceFragment extends PreferenceFragmentCompat implements
      OnSharedPreferenceChangeListener {

    @Override
    public void onResume() {

      super.onResume();
      getPreferenceScreen().getSharedPreferences()
          .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {

      super.onPause();
      getPreferenceScreen().getSharedPreferences()
          .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

      String remoteDatastore = getString(R.string.pref_remote_datastore);
      if (s.equals(remoteDatastore)) {
        SharedPreferences preferences = PreferenceManager
            .getDefaultSharedPreferences(getContext());

        boolean isRemoteDatastore = preferences.getBoolean(remoteDatastore, false);

        if (isRemoteDatastore) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
          builder
              .setMessage(R.string.msg_change_to_airtable_confirmation)
              .setPositiveButton(R.string.action_yes,
                  (dialog, id) -> App.get(getContext()).invalidateDataAndRestart())
              .setNegativeButton(R.string.action_no,
                  (dialog, id) -> {
                    SwitchPreference prefRemoteDatastore = (SwitchPreference) getPreferenceScreen()
                        .findPreference(remoteDatastore);
                    prefRemoteDatastore.setChecked(false);
                  });
          builder.create().show();
        }
      }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

      setPreferencesFromResource(R.xml.settings, rootKey);

      OnPreferenceChangeListener textEmptyErrorListener = (preference, newValue) -> {

        if (TextUtils.isEmpty(newValue.toString())) {

          Toast.makeText(getActivity(), R.string.msg_error_text_can_not_be_empty,
              Toast.LENGTH_LONG).show();

          return false;
        }
        return true;
      };

      String airtableKey = getString(R.string.pref_airtable_api_key);
      getPreferenceScreen()
          .findPreference(airtableKey).setOnPreferenceChangeListener(textEmptyErrorListener);

      String airtableBase = getString(R.string.pref_airtable_base_id);
      getPreferenceScreen()
          .findPreference(airtableBase).setOnPreferenceChangeListener(textEmptyErrorListener);

      String remoteDatastore = getString(R.string.pref_remote_datastore);
      getPreferenceScreen().findPreference(remoteDatastore)
          .setOnPreferenceChangeListener((preference, newValue) -> {

            Timber.d("%s", newValue.toString());

            SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
            if (preferences.contains(airtableKey) && preferences.contains(airtableBase)) {

              return true;
            } else {

              Snackbar
                  .make(getView(), R.string.msg_error_must_fill_airtable_data, Snackbar.LENGTH_LONG)
                  .show();

              return false;
            }
          });
    }
  }
}
