package com.clloret.days.screenshots;

import java.util.Locale;
import org.junit.ClassRule;
import tools.fastlane.screengrab.locale.LocaleTestRule;

public class ScreenshotsEsEsTest extends BaseScreenshotsTest {

  @ClassRule
  public static final LocaleTestRule localeTestRule = new LocaleTestRule(
      new Locale("es", "ES"), DEVICE_LOCALE
  );

}
