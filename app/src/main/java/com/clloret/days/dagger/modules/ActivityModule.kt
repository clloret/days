package com.clloret.days.dagger.modules

import com.clloret.days.activities.MainActivity
import com.clloret.days.dagger.scopes.ActivityScope
import com.clloret.days.events.create.EventCreateActivity
import com.clloret.days.events.edit.EventEditActivity
import com.clloret.days.events.list.EventListFragment
import com.clloret.days.menu.MenuFragment
import com.clloret.days.tags.create.TagCreateActivity
import com.clloret.days.tags.edit.TagEditActivity
import com.clloret.days.tasker.ui.TaskerSelectEventActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindMainActivity(): MainActivity

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindEventCreateActivity(): EventCreateActivity

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindEventEditActivity(): EventEditActivity

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindEventListFragment(): EventListFragment

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindMenuFragment(): MenuFragment

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindTagCreateActivity(): TagCreateActivity

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindTagEditActivity(): TagEditActivity

  @ContributesAndroidInjector
  @ActivityScope
  abstract fun bindTaskerSelectEventActivity(): TaskerSelectEventActivity

}