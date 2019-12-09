package com.clloret.days.screenshots.screenshot;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

import androidx.test.runner.screenshot.BasicScreenCaptureProcessor;
import java.io.File;

public class ScreenshotCaptureProcessor extends BasicScreenCaptureProcessor {

  ScreenshotCaptureProcessor(String parentFolder) {

    super();

    this.mDefaultScreenshotPath = new File(
        new File(getExternalStoragePublicDirectory(DIRECTORY_PICTURES),
            "DaysScreenshots").getAbsolutePath(), parentFolder);
  }

  @Override
  protected String getFilename(String prefix) {

    return prefix;
  }
}
