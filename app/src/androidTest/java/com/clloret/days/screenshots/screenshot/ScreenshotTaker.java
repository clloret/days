package com.clloret.days.screenshots.screenshot;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;
import androidx.test.runner.screenshot.Screenshot;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ScreenshotTaker {

  static void takeScreenshot(String parentFolder, String screenshotName) {

    ScreenCapture screenCapture = Screenshot.capture();
    ScreenshotCaptureProcessor screenshotCaptureProcessor = new ScreenshotCaptureProcessor(
        parentFolder);
    Set<ScreenCaptureProcessor> processors = new HashSet<>(
        Collections.singletonList(screenshotCaptureProcessor));
    try {
      screenCapture.setName(screenshotName);
      screenCapture.process(processors);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
