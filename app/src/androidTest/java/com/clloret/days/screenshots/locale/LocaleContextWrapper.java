package com.clloret.days.screenshots.locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;
import java.util.Locale;

public class LocaleContextWrapper extends ContextWrapper {

  public LocaleContextWrapper(Context base) {

    super(base);
  }

  public static ContextWrapper wrap(Context context, Locale locale) {

    Configuration config = context.getResources().getConfiguration();
    Locale sysLocale;
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
      sysLocale = getSystemLocale(config);
    } else {
      sysLocale = getSystemLocaleLegacy(config);
    }

    if (locale != null && !sysLocale.equals(locale)) {
      Locale.setDefault(locale);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setSystemLocale(config, locale);
      } else {
        setSystemLocaleLegacy(config, locale);
      }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      context = context.createConfigurationContext(config);
    } else {
      context.getResources()
          .updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
    return new LocaleContextWrapper(context);
  }

  public static Locale getSystemLocaleLegacy(Configuration config) {

    return config.locale;
  }

  @TargetApi(Build.VERSION_CODES.N)
  public static Locale getSystemLocale(Configuration config) {

    return config.getLocales().get(0);
  }

  public static void setSystemLocaleLegacy(Configuration config, Locale locale) {

    config.locale = locale;
  }

  @TargetApi(Build.VERSION_CODES.N)
  public static void setSystemLocale(Configuration config, Locale locale) {

    config.setLocale(locale);
  }
}
