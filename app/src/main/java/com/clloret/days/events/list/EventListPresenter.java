package com.clloret.days.events.list;

import static com.clloret.days.domain.entities.Event.REMINDER_EVENT_DAY;

import android.support.annotation.NonNull;
import com.clloret.days.device.TimeProvider;
import com.clloret.days.device.events.ReminderListScheduleEvent;
import com.clloret.days.device.events.ReminderScheduleEvent;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.filter.EventFilterStrategy;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.events.EventCreatedEvent;
import com.clloret.days.model.events.EventDeletedEvent;
import com.clloret.days.model.events.EventModifiedEvent;
import com.clloret.days.model.events.ShowMessageEvent;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
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

public class EventListPresenter extends MvpNullObjectBasePresenter<EventListView> {

  private final AppDataStore api;
  private final EventBus eventBus;
  private final EventViewModelMapper eventViewModelMapper;
  private final TimeProvider timeProvider;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public EventListPresenter(AppDataStore api, EventViewModelMapper eventViewModelMapper,
      EventBus eventBus, TimeProvider timeProvider) {

    this.api = api;
    this.eventViewModelMapper = eventViewModelMapper;
    this.eventBus = eventBus;
    this.timeProvider = timeProvider;
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

    EventListView view = getView();

    Single<List<Event>> events = filterStrategy.getEvents(api);

    Disposable subscribe = events
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(result -> {

          Timber.d("getLocalEvents: %d", result.size());

          if (pullToRefresh) {
            eventBus
                .post(new ReminderListScheduleEvent(result, true));
          }

          view.setData(eventViewModelMapper.fromEvent(result));
          view.showContent();
        })
        .doOnError(error -> view.onError(error.getMessage()))
        .subscribe();
    disposable.add(subscribe);
  }

  public void loadEvents(final boolean pullToRefresh, EventFilterStrategy filterStrategy) {

    if (pullToRefresh) {
      EventListView view = getView();

      Disposable subscribe = api.getEvents(true)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(list -> {
            Timber.d("getEvents: %d", list.size());
            getLocalEvents(true, filterStrategy);
          }, error -> view.onError(error.getMessage()));

      disposable.add(subscribe);
    } else {
      getLocalEvents(false, filterStrategy);
    }
  }

  public void deleteEvent(EventViewModel event) {

    EventListView view = getView();

    Event eventToDelete = eventViewModelMapper.toEvent(event);
    Disposable subscribe = api.deleteEvent(eventToDelete)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(deleted -> {
          if (event.hasReminder()) {
            eventBus.post(new ReminderScheduleEvent(eventToDelete, false, false));
          }

          view.deleteSuccessfully(event, deleted);
        })
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();
    disposable.add(subscribe);
  }

  public void editEvent(@NonNull EventViewModel event) {

    getView().showEditEventUi(event);
  }

  public void makeEventFavorite(@NonNull EventViewModel event) {

    EventListView view = getView();

    event.setFavorite(!event.isFavorite());

    Disposable subscribe = api.editEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(result -> view.favoriteSuccessfully(eventViewModelMapper.fromEvent(result)))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();
    disposable.add(subscribe);
  }

  public void undoDelete(@NonNull EventViewModel event) {

    EventListView view = getView();

    Disposable subscribe = api.createEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(
            result -> view.undoDeleteSuccessfully(eventViewModelMapper.fromEvent(result)))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();
    disposable.add(subscribe);
  }

  public void resetDate(EventViewModel event) {

    EventListView view = getView();

    LocalDate date = timeProvider.getCurrentDate();
    event.setDate(date.toDate());

    Disposable subscribe = api.editEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(
            result -> {
              if (result.hasReminder()) {
                eventBus.post(new ReminderScheduleEvent(result, true, true));
              }
              view.dateResetSuccessfully(eventViewModelMapper.fromEvent(result));
            })
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();
    disposable.add(subscribe);
  }

  public void toggleEventReminder(EventViewModel event) {

    EventListView view = getView();
    final boolean removePreviously = event.hasReminder();

    if (event.hasReminder()) {
      event.setReminder(null);
      event.setReminderUnit(null);
    } else {
      event.setReminder(REMINDER_EVENT_DAY);
      event.setReminderUnit(Event.TimeUnit.DAY);
    }

    Disposable subscribe = api.editEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(
            result -> {
              EventViewModel eventViewModel = eventViewModelMapper.fromEvent(result);
              eventBus.post(new ReminderScheduleEvent(result, eventViewModel.hasReminder(),
                  removePreviously));

              view.reminderSuccessfully(eventViewModel);
            })
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();
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
