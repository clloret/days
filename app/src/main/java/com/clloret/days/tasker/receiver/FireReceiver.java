package com.clloret.days.tasker.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.clloret.days.device.receivers.RefreshReceiver;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.interactors.events.EditEventUseCase;
import com.clloret.days.domain.interactors.events.EditEventUseCase.RequestValues;
import com.clloret.days.domain.interactors.events.GetEventUseCase;
import com.clloret.days.domain.utils.DateUtils;
import com.clloret.days.domain.utils.StringUtils;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.tasker.bundle.CommonBundle;
import com.clloret.days.tasker.bundle.EventCreateBundle;
import com.clloret.days.tasker.bundle.EventEditBundle;
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.Date;
import javax.inject.Inject;
import timber.log.Timber;

public class FireReceiver extends AbstractPluginSettingReceiver {

  @Inject
  CreateEventUseCase createEventUseCase;

  @Inject
  EditEventUseCase editEventUseCase;

  @Inject
  GetEventUseCase getEventUseCase;

  @Inject
  EventViewModelMapper eventViewModelMapper;

  @Inject
  TimeProvider timeProvider;

  private Context context;

  @Override
  protected boolean isBundleValid(@NonNull Bundle bundle) {

    Timber.d("isBundleValid");

    final String bundleString = bundle.getString(CommonBundle.EXTRA_BUNDLE);

    if (bundleString == null) {
      return false;
    }

    switch (bundleString) {

      case EventCreateBundle.BUNDLE_ID:
        return EventCreateBundle.Companion.isBundleValid(bundle);

      case EventEditBundle.BUNDLE_ID:
        return EventEditBundle.Companion.isBundleValid(bundle);

      default:
        return false;
    }
  }

  @Override
  protected boolean isAsync() {

    Timber.d("isAsync");

    return false;
  }

  @Override
  protected void firePluginSetting(@NonNull final Context context, @NonNull final Bundle bundle) {

    Timber.d("firePluginSetting");

    this.context = context;

    AndroidInjection.inject(this, context);

    final String bundleString = bundle.getString(CommonBundle.EXTRA_BUNDLE);

    if (bundleString == null) {
      return;
    }

    switch (bundleString) {

      case EventCreateBundle.BUNDLE_ID:
        createEventFromBundle(bundle);
        break;

      case EventEditBundle.BUNDLE_ID:
        editEventFromBundle(bundle);
        break;

      default:
        Timber.w("Invalid bundle: %s", bundle);
        break;
    }
  }

  private void createEventFromBundle(Bundle bundle) {

    final EventCreateBundle createBundle = new EventCreateBundle(bundle);

    final Date date = DateUtils
        .getDateFromString(createBundle.getDate(), timeProvider.getCurrentDate());

    final Event event = new EventBuilder()
        .setName(createBundle.getName())
        .setDescription(createBundle.getDescription())
        .setDate(date)
        .setReminder(StringUtils.tryParseInt(createBundle.getReminder()))
        .setFavorite("1".equals(createBundle.getFavorite()))
        .build();

    createEventUseCase.execute(event)
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .map(eventViewModelMapper::fromEvent)
        .doOnSuccess(this::sendMessage)
        .doOnError(Timber::e)
        .onErrorComplete()
        .subscribe();
  }

  @SuppressLint("CheckResult")
  private void editEventFromBundle(@NonNull final Bundle bundle) {

    final EventEditBundle editBundle = new EventEditBundle(bundle);

    //noinspection ResultOfMethodCallIgnored
    getEventUseCase.execute(editBundle.getId())
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(event -> editOriginalEvent(event, editBundle), Timber::e);
  }

  private void editOriginalEvent(Event originalEvent, EventEditBundle editBundle) {

    final Event modifiedEvent = originalEvent.clone();

    if (editBundle.getName() != null) {
      modifiedEvent.setName(editBundle.getName());
    }

    if (editBundle.getDescription() != null) {
      modifiedEvent.setDescription(editBundle.getDescription());
    }

    if (editBundle.getDate() != null) {
      final Date date = DateUtils
          .getDateFromString(editBundle.getDate(), timeProvider.getCurrentDate());
      modifiedEvent.setDate(date);
    }

    if (editBundle.getReminder() != null) {
      modifiedEvent.setReminder(StringUtils.tryParseInt(editBundle.getReminder()));
    }

    if (editBundle.getFavorite() != null) {
      modifiedEvent.setFavorite("1".equals(editBundle.getFavorite()));
    }

    final RequestValues requestValues = new RequestValues(modifiedEvent, originalEvent);

    editEventUseCase.execute(requestValues)
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .map(eventViewModelMapper::fromEvent)
        .doOnSuccess(this::sendMessage)
        .doOnError(Timber::e)
        .onErrorComplete()
        .subscribe();
  }

  private void sendMessage(EventViewModel eventViewModel) {

    Intent intent = new Intent(context, RefreshReceiver.class);
    context.sendBroadcast(intent);
  }

}

