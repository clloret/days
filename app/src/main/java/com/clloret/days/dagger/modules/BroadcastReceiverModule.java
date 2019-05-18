package com.clloret.days.dagger.modules;

import com.clloret.days.device.receivers.FirstStartBroadcastReceiver;
import com.clloret.days.device.receivers.ReminderReceiver;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BroadcastReceiverModule {

  @ContributesAndroidInjector
  abstract FirstStartBroadcastReceiver bindBootBroadcastReceiver();

  @ContributesAndroidInjector
  abstract ReminderReceiver bindReminderReceiver();

}