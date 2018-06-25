package com.clloret.days.events.create;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.EventBuilder;
import com.clloret.days.model.entities.Tag;
import com.clloret.days.utils.SelectionMap;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import javax.inject.Inject;

public class EventCreatePresenter extends MvpBasePresenter<EventCreateView> {

  private final AppDataStore api;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public EventCreatePresenter(AppDataStore api) {

    this.api = api;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void createEvent(@NonNull String name, @NonNull String description, @Nullable Date date,
      @NonNull SelectionMap<String, Tag> mapTags) {

    if (name.isEmpty()) {
      getView().onEmptyEventNameError();
      return;
    }

    if (date == null) {
      getView().onEmptyEventDateError();
      return;
    }

    String[] tags = mapTags.getKeySelection(Tag::getId)
        .toArray(new String[0]);

    Event newEvent = new EventBuilder()
        .setName(name)
        .setDescription(description)
        .setDate(date)
        .setTags(tags)
        .build();

    Disposable subscribe = api.createEvent(newEvent)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> getView().onSuccessfully(result),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }

  public void loadTags() {

    Disposable subscribe = api.getTags(false)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tags -> {
          if (isViewAttached()) {
            getView().setData(tags);
          }
        }, error -> {
          if (isViewAttached()) {
            getView().showError(error);
          }
        });
    disposable.add(subscribe);
  }

}