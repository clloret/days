package com.clloret.days.screenshots;

import java.util.Locale;
import org.junit.ClassRule;
import tools.fastlane.screengrab.locale.LocaleTestRule;

public class ScreenshotsEnUsTest extends BaseScreenshotsTest {

  @ClassRule
  public static final LocaleTestRule localeTestRule = new LocaleTestRule(
      new Locale("en", "US"), DEVICE_LOCALE
  );

}
