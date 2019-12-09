package com.clloret.days.utils;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.test.runner.AndroidJUnitRunner;
import com.clloret.days.TestApp;
import java.util.Locale;

@SuppressWarnings("unused")
public class CustomTestRunner extends AndroidJUnitRunner {

  private TestApp application;

  @Override
  public void onCreate(Bundle arguments) {

    super.onCreate(arguments);

    String locale = arguments.getString("locale");
    if (locale != null) {
      Locale forLanguageTag = Locale.forLanguageTag(locale);
      application.setLocale(forLanguageTag);
    }
  }

  @Override
  public Application newApplication(ClassLoader cl, String className, Context context)
      throws IllegalAccessException, ClassNotFoundException, InstantiationException {

    application = (TestApp) super.newApplication(cl, TestApp.class.getName(), context);

    return application;
  }

}
