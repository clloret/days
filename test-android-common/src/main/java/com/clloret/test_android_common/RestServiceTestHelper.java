package com.clloret.test_android_common;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RestServiceTestHelper {

  public static String convertStreamToString(InputStream is) throws Exception {

    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append('\n');
    }
    reader.close();
    return sb.toString();
  }

  public static String getStringFromFile(Context context, String filePath) throws Exception {

    final InputStream stream = context.getResources().getAssets().open(filePath);

    String ret = convertStreamToString(stream);
    //Make sure you close all streams.
    stream.close();
    return ret;
  }

  public static String readFromInputStream(String fileName)
      throws IOException {

    InputStream inputStream = RestServiceTestHelper.class.getClassLoader()
        .getResourceAsStream(fileName);
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br
        = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append('\n');
      }
    }
    return resultStringBuilder.toString();
  }

}
