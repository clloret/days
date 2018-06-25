package com.clloret.days.events.edit;

import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class EventEditPresenter extends MvpBasePresenter<EventEditView> {

  private final AppDataStore api;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public EventEditPresenter(AppDataStore api) {

    this.api = api;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void saveEvent(Event event) {

    if (event.getName().isEmpty()) {
      getView().onEmptyEventNameError();
      return;
    }

    Disposable subscribe = api.editEvent(event)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> getView().onSuccessfully(result),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }

  public void deleteEvent(Event event) {

    Disposable subscribe = api.deleteEvent(event)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(deleted -> getView().deleteSuccessfully(event, deleted), error -> {
          if (isViewAttached()) {
            getView().onError(error.getMessage());
          }
        });
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