package com.clloret.days.screenshots.locale;

import android.app.backup.BackupManager;
import android.content.res.Configuration;
import android.util.Log;
import java.util.Locale;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ClassLocaleRule implements TestRule {

  private final Locale testLocale;
  private Locale deviceLocale;

  public ClassLocaleRule(Locale testLocale, Locale deviceLocale) {

    this.testLocale = testLocale;
    this.deviceLocale = deviceLocale;
  }

  @Override
  public Statement apply(final Statement base, Description description) {

    return new Statement() {
      public void evaluate() throws Throwable {

        try {
          if (testLocale != null) {
            changeLanguageSettings(testLocale);
          }

          base.evaluate();
        } finally {
          if (deviceLocale != null) {
            changeLanguageSettings(deviceLocale);
          }
        }
      }
    };
  }

  private static void changeLanguageSettings(Locale language) {

    try {
      Class<?> activityManager = Class.forName("android.app.ActivityManager");
      Object am = activityManager.getMethod("getService").invoke(activityManager);
      Configuration configuration = (Configuration) am.getClass().getMethod("getConfiguration")
          .invoke(am);

      configuration.setLocale(language);
      configuration.getClass().getDeclaredField("userSetLocale").setBoolean(configuration, true);
      am.getClass()
          .getMethod("updatePersistentConfiguration", android.content.res.Configuration.class)
          .invoke(am, configuration);
      BackupManager.dataChanged("com.android.providers.settings");
    } catch (Exception e) {
      Log.e("changeLanguageSettings", "Can't change system language", e);
    }
  }

}