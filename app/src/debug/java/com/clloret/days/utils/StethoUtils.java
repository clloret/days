package com.clloret.days.utils;

import android.app.Application;
import com.facebook.stetho.Stetho;

public class StethoUtils {

  public static void install(Application application) {

    Stetho.initializeWithDefaults(application);
  }

}
