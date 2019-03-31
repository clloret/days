package com.clloret.days;

import com.clloret.days.dagger.AppTestComponent;
import com.clloret.days.dagger.DaggerAppTestComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class TestApp extends App {

  private AppTestComponent appComponent;

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

    appComponent = DaggerAppTestComponent
        .builder()
        .application(this)
        .build();
    appComponent.inject(this);

    return appComponent;
  }

  public AppTestComponent getAppComponent() {

    return appComponent;
  }

}
