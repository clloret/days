package com.clloret.days.events.list;

import android.support.annotation.NonNull;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.events.list.filter.EventFilterStrategy;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.events.EventCreatedEvent;
import com.clloret.days.model.events.EventDeletedEvent;
import com.clloret.days.model.events.EventModifiedEvent;
import com.clloret.days.model.events.ShowMessageEvent;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.LocalDate;
import timber.log.Timber;

public class EventListPresenter extends MvpBasePresenter<EventListView> {

  private final AppDataStore api;
  private final CompositeDisposable disposable = new CompositeDisposable();
  private final EventBus eventBus;
  // TODO: 16/10/2018 DI
  private EventViewModelMapper eventViewModelMapper = new EventViewModelMapper();

  @Inject
  public EventListPresenter(AppDataStore api, EventBus eventBus) {

    this.api = api;
    this.eventBus = eventBus;
  }

  @Override
  public void attachView(EventListView view) {

    super.attachView(view);
    eventBus.register(this);
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    eventBus.unregister(this);
    disposable.dispose();
  }

  private void getLocalEvents(final boolean pullToRefresh, EventFilterStrategy filterStrategy) {

    Single<List<Event>> events = filterStrategy.getEvents(api);

    Disposable subscribe = events
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          Timber.d("getLocalEvents: %d", result.size());
          if (isViewAttached()) {
            getView().setData(eventViewModelMapper.fromEvent(result));
            getView().showContent();
          }
        }, error -> {
          if (isViewAttached()) {
            getView().showError(error, pullToRefresh);
          }
        });
    disposable.add(subscribe);
  }

  public void loadEvents(final boolean pullToRefresh, EventFilterStrategy filterStrategy) {

    if (pullToRefresh) {
      api.getEvents(true)
          .doOnSuccess(list -> {
            Timber.d("getEvents: %d", list.size());
            getLocalEvents(true, filterStrategy);
          })
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe();
    } else {
      getLocalEvents(false, filterStrategy);
    }
  }

  public void deleteEvent(EventViewModel event) {

    Disposable subscribe = api.deleteEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(deleted -> getView().deleteSuccessfully(event, deleted), error -> {
          if (isViewAttached()) {
            getView().onError(error.getMessage());
          }
        });
    disposable.add(subscribe);
  }

  public void editEvent(@NonNull EventViewModel event) {

    getView().showEditEventUi(event);
  }

  public void makeEventFavorite(@NonNull EventViewModel event) {

    event.setFavorite(!event.isFavorite());

    Disposable subscribe = api.editEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> getView().favoriteSuccessfully(eventViewModelMapper.fromEvent(result)),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }

  public void undoDelete(@NonNull EventViewModel event) {

    Disposable subscribe = api.createEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            result -> getView().undoDeleteSuccessfully(eventViewModelMapper.fromEvent(result)),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }

  public void resetDate(EventViewModel event) {

    LocalDate date = LocalDate.now();
    event.setDate(date.toDate());

    Disposable subscribe = api.editEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            result -> getView().dateResetSuccessfully(eventViewModelMapper.fromEvent(result)),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(EventCreatedEvent event) {

    getView().showCreatedEvent(event.event);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(EventModifiedEvent event) {

    getView().updateSuccessfully(event.event);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(EventDeletedEvent event) {

    getView().deleteSuccessfully(event.event, true);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(ShowMessageEvent event) {

    getView().showMessage(event.message);
  }
}
