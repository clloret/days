package com.clloret.days.device.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.clloret.days.device.eventbus.EventsUpdatedEvent;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import timber.log.Timber;

public class RefreshReceiver extends BroadcastReceiver {

  @Inject
  EventBus eventBus;

  @Override
  public void onReceive(Context context, Intent intent) {

    Timber.d("onReceive");

    AndroidInjection.inject(this, context);

    eventBus.post(new EventsUpdatedEvent());
  }
}
