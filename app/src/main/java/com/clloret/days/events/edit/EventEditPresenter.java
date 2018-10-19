package com.clloret.days.events.edit;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class EventEditPresenter extends MvpBasePresenter<EventEditView> {

  private final AppDataStore api;
  private final CompositeDisposable disposable = new CompositeDisposable();
  // TODO: 16/10/2018 DI
  private EventViewModelMapper eventViewModelMapper = new EventViewModelMapper();
  private TagViewModelMapper tagViewModelMapper = new TagViewModelMapper();

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
        .subscribe(result -> getView().onSuccessfully(eventViewModelMapper.fromEvent(result)),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }

  public void deleteEvent(Event event) {

    Disposable subscribe = api.deleteEvent(event)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            deleted -> getView().deleteSuccessfully(eventViewModelMapper.fromEvent(event), deleted),
            error -> {
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
            getView().setData(tagViewModelMapper.fromTag(tags));
          }
        }, error -> {
          if (isViewAttached()) {
            getView().showError(error);
          }
        });
    disposable.add(subscribe);
  }

}