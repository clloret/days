package com.clloret.days.dagger.modules;

import com.clloret.days.device.receivers.FirstStartBroadcastReceiver;
import com.clloret.days.device.receivers.ReminderReceiver;
import com.clloret.days.tasker.receiver.FireReceiver;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BroadcastReceiverModule {

  @ContributesAndroidInjector
  abstract FirstStartBroadcastReceiver bindBootBroadcastReceiver();

  @ContributesAndroidInjector
  abstract ReminderReceiver bindReminderReceiver();

  @ContributesAndroidInjector
  abstract FireReceiver bindFireReceiver();

}