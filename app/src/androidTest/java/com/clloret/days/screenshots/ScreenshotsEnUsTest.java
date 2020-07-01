package com.clloret.days.screenshots;

import com.clloret.days.screenshots.screenshot.ScreenshotTakingRule.ScreenshotSuffix;
import java.util.Locale;
import org.junit.ClassRule;
import tools.fastlane.screengrab.locale.LocaleTestRule;

@ScreenshotSuffix("en-US")
public class ScreenshotsEnUsTest extends BaseScreenshotsTest {

  @ClassRule
  public static final LocaleTestRule localeTestRule = new LocaleTestRule(
      new Locale("en", "US"), DEVICE_LOCALE
  );

}
