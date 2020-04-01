package com.clloret.days;

import com.clloret.days.dagger.AppTestComponent;
import com.clloret.days.dagger.DaggerAppTestComponent;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import java.util.Locale;

public class TestApp extends App {

  private AppTestComponent appComponent;
  private Locale locale = Locale.getDefault();

  @Override
  protected AndroidInjector<? extends DaggerApplication> applicationInjector() {

    appComponent = DaggerAppTestComponent
        .builder()
        .application(this)
        .locale(locale)
        .build();
    appComponent.inject(this);

    return appComponent;
  }

  public AppTestComponent getAppComponent() {

    return appComponent;
  }

  public void setLocale(Locale locale) {

    this.locale = locale;
  }

}
