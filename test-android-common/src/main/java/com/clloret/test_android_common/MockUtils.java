package com.clloret.test_android_common;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class MockUtils {

  private final MockWebServer server;

  public MockUtils(MockWebServer server) {

    this.server = server;
  }

  public void enqueueMockResponse(int code, String fileName) throws IOException {

    MockResponse mockResponse = new MockResponse();
    String fileContent = RestServiceTestHelper.readFromInputStream(fileName);
    mockResponse.setResponseCode(code);
    mockResponse.setBody(fileContent);
    server.enqueue(mockResponse);
  }

}
