package com.clloret.days.screenshots.screenshot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class ScreenshotTakingRule extends TestWatcher {

  @Override
  protected void succeeded(Description description) {

    final ScreenshotSuffix screenshotSuffixAnnotation =
        description.getTestClass().getAnnotation(ScreenshotSuffix.class);

    if (screenshotSuffixAnnotation == null) {
      throw new IllegalArgumentException("ScreenshotSuffix class annotation is mandatory");
    }

    final String suffixFromTestMethod =
        screenshotSuffixAnnotation.value();

    String screenshotName = description.getMethodName() + "_" + suffixFromTestMethod;

    ScreenshotTaker.takeScreenshot(suffixFromTestMethod, screenshotName);
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public @interface ScreenshotSuffix {

    String value() default "";
  }

}
