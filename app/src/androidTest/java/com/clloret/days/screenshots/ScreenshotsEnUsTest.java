package com.clloret.days.screenshots;

import com.clloret.days.screenshots.locale.ClassLocaleRule;
import com.clloret.days.screenshots.screenshot.ScreenshotTakingRule.ScreenshotSuffix;
import java.util.Locale;
import org.junit.ClassRule;

@ScreenshotSuffix("en-US")
public class ScreenshotsEnUsTest extends BaseScreenshotsTest {

  @ClassRule
  public static final ClassLocaleRule CLASS_LOCALE_RULE = new ClassLocaleRule(
      new Locale("en", "US"), DEVICE_LOCALE);
}
