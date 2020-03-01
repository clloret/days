package com.clloret.days.tasker.receiver;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.utils.DateUtils;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.tasker.bundle.EventCreateBundle;
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;
import dagger.android.AndroidInjection;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import javax.inject.Inject;
import timber.log.Timber;

public class FireReceiver extends AbstractPluginSettingReceiver {

  @Inject
  CreateEventUseCase createEventUseCase;

  @Inject
  TimeProvider timeProvider;

  @Override
  protected boolean isBundleValid(@NonNull Bundle bundle) {

    Timber.d("isBundleValid");

    return EventCreateBundle.Companion.isBundleValid(bundle);
  }

  @Override
  protected boolean isAsync() {

    Timber.d("isAsync");

    return false;
  }

  @Override
  protected void firePluginSetting(@NonNull Context context, @NonNull Bundle bundle) {

    Timber.d("firePluginSetting");

    AndroidInjection.inject(this, context);

    String name = bundle.getString(EventCreateBundle.EXTRA_NAME);
    String description = bundle.getString(EventCreateBundle.EXTRA_DESCRIPTION);
    String strDate = bundle.getString(EventCreateBundle.EXTRA_DATE);
    Date date = DateUtils.getDateFromString(strDate, timeProvider.getCurrentDate());

    Toast.makeText(context, name, Toast.LENGTH_LONG).show();
    Toast.makeText(context, date.toString(), Toast.LENGTH_LONG).show();

    Event event = new EventBuilder()
        .setName(name)
        .setDescription(description)
        .setDate(date)
        .build();

    createEventUseCase.execute(event)
        .subscribeOn(Schedulers.io())
        .doOnError(Timber::e)
        .onErrorComplete()
        .subscribe();
  }

}

