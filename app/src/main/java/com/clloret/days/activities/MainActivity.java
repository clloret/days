package com.clloret.days.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
    implements OnProgressListener, OnFragmentLifecycleListener {

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
  FloatingActionButton actionNewEvent;

  private ActionBarDrawerToggle drawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    injectDependencies();

    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    configureNavigationDrawer();

    actionNewEvent.setOnClickListener(v -> {

      Optional<TagViewModel> selectedTag = getNavigationDrawerFragment().getSelectedTag();
      navigator.navigateToEventCreate(this, selectedTag);
    });

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
  public void onConfigurationChanged(Configuration newConfig) {

    super.onConfigurationChanged(newConfig);

    drawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {

    outState.putString(STATE_TITLE, getTitle().toString());

    super.onSaveInstanceState(outState);
  }

  @Override
  public void onAttachFragment(Fragment fragment) {

    if (fragment instanceof EventListFragment) {
      EventListFragment eventListFragment = (EventListFragment) fragment;
      eventListFragment.setOnProgressListener(this);
      eventListFragment.setOnFragmentLifecycleListener(this);
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    String title = savedInstanceState.getString("title");
    setTitle(title);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
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

    if (!actionNewEvent.isShown()) {
      actionNewEvent.show();
    }
  }
}
