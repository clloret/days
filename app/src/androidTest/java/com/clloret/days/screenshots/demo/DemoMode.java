package com.clloret.days.screenshots.demo;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

public class DemoMode {

  private final Context context;

  public DemoMode(Context context) {

    this.context = context;
  }

  @NonNull
  private Intent getDemoModeIntent() {

    return new Intent("com.android.systemui.demo");
  }

  private void send(Intent intent) {

    context.sendBroadcast(intent);
  }

  public DemoMode enter() {

    Intent intent = getDemoModeIntent();
    intent.putExtra("command", "enter");
    send(intent);

    return this;
  }

  public void exit() {

    Intent intent = getDemoModeIntent();
    intent.putExtra("command", "exit");
    send(intent);
  }

  public DemoMode setClock() {

    Intent intent = getDemoModeIntent();
    intent.putExtra("command", "clock");
    intent.putExtra("hhmm", "1200");
    send(intent);

    return this;
  }

  public DemoMode setNetwork() {

    Intent intent = getDemoModeIntent();
    intent.putExtra("command", "network");
    intent.putExtra("mobile", "show");
    intent.putExtra("level", "4");
    send(intent);

    return this;
  }

  public DemoMode hideNotifications() {

    Intent intent = getDemoModeIntent();
    intent.putExtra("command", "notifications");
    intent.putExtra("visible", "false");
    send(intent);

    return this;
  }

}
