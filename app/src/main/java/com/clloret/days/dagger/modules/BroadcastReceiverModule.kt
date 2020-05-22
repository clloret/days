package com.clloret.days.dagger.modules

import com.clloret.days.device.receivers.FirstStartBroadcastReceiver
import com.clloret.days.device.receivers.RefreshReceiver
import com.clloret.days.device.receivers.ReminderReceiver
import com.clloret.days.tasker.receiver.FireReceiver
import com.clloret.days.widget.DaysWidget
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {

  @ContributesAndroidInjector
  abstract fun bindBootBroadcastReceiver(): FirstStartBroadcastReceiver

  @ContributesAndroidInjector
  abstract fun bindReminderReceiver(): ReminderReceiver

  @ContributesAndroidInjector
  abstract fun bindFireReceiver(): FireReceiver

  @ContributesAndroidInjector
  abstract fun bindRefreshReceiver(): RefreshReceiver

  @ContributesAndroidInjector
  abstract fun bindDaysWidget(): DaysWidget

}