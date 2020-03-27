package com.clloret.days.dagger.modules;

import com.clloret.days.activities.MainActivity;
import com.clloret.days.dagger.scopes.ActivityScope;
import com.clloret.days.events.create.EventCreateActivity;
import com.clloret.days.events.edit.EventEditActivity;
import com.clloret.days.events.list.EventListFragment;
import com.clloret.days.menu.MenuFragment;
import com.clloret.days.tags.create.TagCreateActivity;
import com.clloret.days.tags.edit.TagEditActivity;
import com.clloret.days.tasker.ui.TaskerSelectEventActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

  @ContributesAndroidInjector()
  @ActivityScope
  abstract MainActivity bindMainActivity();

  @ContributesAndroidInjector()
  @ActivityScope
  abstract EventCreateActivity bindEventCreateActivity();

  @ContributesAndroidInjector()
  @ActivityScope
  abstract EventEditActivity bindEventEditActivity();

  @ContributesAndroidInjector()
  @ActivityScope
  abstract EventListFragment bindEventListFragment();

  @ContributesAndroidInjector()
  @ActivityScope
  abstract MenuFragment bindMenuFragment();

  @ContributesAndroidInjector()
  @ActivityScope
  abstract TagCreateActivity bindTagCreateActivity();

  @ContributesAndroidInjector()
  @ActivityScope
  abstract TagEditActivity bindTagEditActivity();

  @ContributesAndroidInjector()
  @ActivityScope
  abstract TaskerSelectEventActivity bindTaskerSelectEventActivity();

}
