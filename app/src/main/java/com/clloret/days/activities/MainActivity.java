package com.clloret.days.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.clloret.days.Navigator;
import com.clloret.days.R;
import com.clloret.days.domain.utils.Optional;
import com.clloret.days.events.list.EventListFragment;
import com.clloret.days.events.list.EventListFragment.OnFragmentLifecycleListener;
import com.clloret.days.events.list.EventListFragment.OnProgressListener;
import com.clloret.days.menu.MenuFragment;
import com.clloret.days.model.entities.TagViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import dagger.android.AndroidInjection;
import java.util.ArrayList;
import java.util.Locale;
import javax.inject.Inject;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
    implements OnProgressListener, OnFragmentLifecycleListener {

  private static final int REQUEST_CODE_SPEECH_INPUT = 0x01;
  private static final String STATE_TITLE = "title";

  @Inject
  Navigator navigator;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  @BindView(R.id.drawer_layout)
  DrawerLayout drawerLayout;

  @BindView(R.id.fab_main_newevent)
  FloatingActionButton buttonNewEvent;

  private ActionBarDrawerToggle drawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    injectDependencies();

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    configureNavigationDrawer();

    configureButtonNewEvent();

    if (savedInstanceState == null) {
      showMainView();
    }
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {

    super.onPostCreate(savedInstanceState);

    drawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(@NonNull Configuration newConfig) {

    super.onConfigurationChanged(newConfig);

    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {

    outState.putString(STATE_TITLE, getTitle().toString());

    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    String title = savedInstanceState.getString("title");
    setTitle(title);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    //noinspection SwitchStatementWithTooFewBranches
    switch (item.getItemId()) {

      case R.id.menu_speech_recognition:
        startRecognizeSpeech();
        return true;

      default:
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
      if (resultCode == RESULT_OK && null != data) {
        final ArrayList<String> result = data
            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        final String recognizedText = result.get(0);

        Timber.i(recognizedText);

        Optional<TagViewModel> selectedTag = getNavigationDrawerFragment().getSelectedTag();
        navigator.navigateToEventCreate(this, Optional.of(recognizedText), selectedTag);
      }
    }

  }

  @Override
  public void onAttachFragment(@NonNull Fragment fragment) {

    if (fragment instanceof EventListFragment) {
      EventListFragment eventListFragment = (EventListFragment) fragment;
      eventListFragment.setOnProgressListener(this);
      eventListFragment.setOnFragmentLifecycleListener(this);
    }
  }

  @Override
  public void showIndeterminateProgress() {

    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideIndeterminateProgress() {

    progressBar.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onStartFragment() {

    Timber.d("onStartFragment");

    if (!buttonNewEvent.isShown()) {
      buttonNewEvent.show();
    }
  }

  private void configureButtonNewEvent() {

    buttonNewEvent.setOnClickListener(v -> {

      Optional<TagViewModel> selectedTag = getNavigationDrawerFragment().getSelectedTag();
      navigator.navigateToEventCreate(this, Optional.empty(), selectedTag);
    });
  }

  private void configureNavigationDrawer() {

    MenuFragment navigationDrawer = getNavigationDrawerFragment();
    navigationDrawer.configure(drawerLayout);

    drawerToggle = setupDrawerToggle();

    drawerLayout.addDrawerListener(drawerToggle);
  }

  private void showMainView() {

    MenuFragment navigationDrawer = getNavigationDrawerFragment();
    navigationDrawer.showMainView();
  }

  private MenuFragment getNavigationDrawerFragment() {

    return (MenuFragment) getSupportFragmentManager()
        .findFragmentById(R.id.navigation_drawer);
  }

  private ActionBarDrawerToggle setupDrawerToggle() {

    return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.action_drawer_open,
        R.string.action_drawer_close);
  }

  private void injectDependencies() {

    AndroidInjection.inject(this);
  }

  private void startRecognizeSpeech() {

    final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    intent
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.msg_create_event_with_voice));

    try {
      startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
    } catch (ActivityNotFoundException a) {
      showSnackBar(R.string.msg_error_voice_recognition_not_supported);
    }
  }

  private void showSnackBar(@StringRes int resId) {

    View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
    String message = getString(resId);
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        .show();
  }
}
