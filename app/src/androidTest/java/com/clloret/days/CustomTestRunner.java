package com.clloret.days;

import android.app.Application;
import android.content.Context;
import androidx.test.runner.AndroidJUnitRunner;

@SuppressWarnings("unused")
public class CustomTestRunner extends AndroidJUnitRunner {

  @Override
  public Application newApplication(ClassLoader cl, String className, Context context)
      throws IllegalAccessException, ClassNotFoundException, InstantiationException {

    return super.newApplication(cl, TestApp.class.getName(), context);
  }

}
