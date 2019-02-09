package com.clloret.days.dagger.modules;

import com.clloret.days.activities.MainActivity;
import com.clloret.days.events.create.EventCreateActivity;
import com.clloret.days.events.edit.EventEditActivity;
import com.clloret.days.events.list.EventListFragment;
import com.clloret.days.menu.MenuFragment;
import com.clloret.days.tags.create.TagCreateActivity;
import com.clloret.days.tags.edit.TagEditActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

  @ContributesAndroidInjector()
  abstract MainActivity bindMainActivity();

  @ContributesAndroidInjector()
  abstract EventCreateActivity bindEventCreateActivity();

  @ContributesAndroidInjector()
  abstract EventEditActivity bindEventEditActivity();

  @ContributesAndroidInjector()
  abstract EventListFragment bindEventListFragment();

  @ContributesAndroidInjector()
  abstract MenuFragment bindMenuFragment();

  @ContributesAndroidInjector()
  abstract TagCreateActivity bindTagCreateActivity();

  @ContributesAndroidInjector()
  abstract TagEditActivity bindTagEditActivity();
}
