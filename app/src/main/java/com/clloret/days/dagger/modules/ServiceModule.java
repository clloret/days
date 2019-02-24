package com.clloret.days.dagger.modules;

import com.clloret.days.dagger.scopes.ServiceScope;
import com.clloret.days.device.services.TimeLapseService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {

  @ServiceScope
  @ContributesAndroidInjector
  abstract TimeLapseService bindTimeLapseService();
}