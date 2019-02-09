package com.clloret.days.dagger.modules;

import com.clloret.days.device.receivers.BootBroadcastReceiver;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BroadcastReceiverModule {

  @ContributesAndroidInjector
  abstract BootBroadcastReceiver bindBootBroadcastReceiver();

}