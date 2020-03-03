package com.clloret.days.tasker.receiver;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.utils.DateUtils;
import com.clloret.days.domain.utils.StringUtils;
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
  protected void firePluginSetting(@NonNull final Context context, @NonNull final Bundle bundle) {

    Timber.d("firePluginSetting");

    AndroidInjection.inject(this, context);

    final EventCreateBundle createBundle = new EventCreateBundle(bundle);

    final Date date = DateUtils
        .getDateFromString(createBundle.getDate(), timeProvider.getCurrentDate());

    final Event event = new EventBuilder()
        .setName(createBundle.getTitle())
        .setDescription(createBundle.getDescription())
        .setDate(date)
        .setReminder(StringUtils.tryParseInt(createBundle.getReminder()))
        .setFavorite("1".equals(createBundle.getFavorite()))
        .build();

    createEventUseCase.execute(event)
        .subscribeOn(Schedulers.io())
        .doOnError(Timber::e)
        .onErrorComplete()
        .subscribe();
  }

}

