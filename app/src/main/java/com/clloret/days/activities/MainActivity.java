package com.clloret.days.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.clloret.days.Navigator;
import com.clloret.days.R;
import com.clloret.days.events.list.EventListFragment;
import com.clloret.days.events.list.EventListFragment.OnProgressListener;
import com.clloret.days.menu.MenuFragment;
import com.clloret.days.model.entities.TagViewModel;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements OnProgressListener {

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
  View actionNewEvent;

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

      TagViewModel selectedTag = getNavigationDrawerFragment().getSelectedTag();
      navigator.navigateToEventCreate(MainActivity.this, selectedTag);
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
      EventListFragment headlinesFragment = (EventListFragment) fragment;
      headlinesFragment.setOnProgressListener(this);
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
    navigationDrawer.setUp(drawerLayout);

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
}
