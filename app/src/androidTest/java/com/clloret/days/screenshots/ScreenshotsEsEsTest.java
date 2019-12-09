package com.clloret.days.screenshots;

import com.clloret.days.screenshots.locale.ClassLocaleRule;
import com.clloret.days.screenshots.screenshot.ScreenshotTakingRule.ScreenshotSuffix;
import java.util.Locale;
import org.junit.ClassRule;

@ScreenshotSuffix("es-ES")
public class ScreenshotsEsEsTest extends BaseScreenshotsTest {

  @ClassRule
  public static final ClassLocaleRule CLASS_LOCALE_RULE = new ClassLocaleRule(
      new Locale("es", "ES"), DEVICE_LOCALE);
}
