package com.clloret.days.events.list;

import androidx.annotation.NonNull;
import com.clloret.days.base.BaseRxPresenter;
import com.clloret.days.device.eventbus.EventsUpdatedEvent;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.filter.EventFilterAll;
import com.clloret.days.domain.events.filter.EventFilterStrategy;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.FavoriteEventUseCase;
import com.clloret.days.domain.interactors.events.GetEventsUseCase;
import com.clloret.days.domain.interactors.events.GetFilteredEventsUseCase;
import com.clloret.days.domain.interactors.events.GetFilteredEventsUseCase.RequestValues;
import com.clloret.days.domain.interactors.events.ResetEventDateUseCase;
import com.clloret.days.domain.interactors.events.ToggleEventReminderUseCase;
import com.clloret.days.domain.utils.StringUtils;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.events.EventCreatedEvent;
import com.clloret.days.model.events.EventDeletedEvent;
import com.clloret.days.model.events.EventModifiedEvent;
import com.clloret.days.model.events.ShowMessageEvent;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import timber.log.Timber;

public class EventListPresenter extends BaseRxPresenter<EventListView> {

  private static final int SEARCH_DEBOUNCE_TIMEOUT = 300;
  private final EventBus eventBus;
  private final EventViewModelMapper eventViewModelMapper;
  private final GetEventsUseCase getEventsUseCase;
  private final GetFilteredEventsUseCase getFilteredEventsUseCase;
  private final FavoriteEventUseCase favoriteEventUseCase;
  private final ResetEventDateUseCase resetEventDateUseCase;
  private final ToggleEventReminderUseCase toggleEventReminderUseCase;
  private final DeleteEventUseCase deleteEventUseCase;
  private final CreateEventUseCase createEventUseCase;
  private EventFilterStrategy filterStrategy = new EventFilterAll();
  private List<EventViewModel> unfilteredEvents;

  @Inject
  public EventListPresenter(
      EventViewModelMapper eventViewModelMapper,
      EventBus eventBus,
      GetEventsUseCase getEventsUseCase,
      GetFilteredEventsUseCase getFilteredEventsUseCase,
      FavoriteEventUseCase favoriteEventUseCase,
      ResetEventDateUseCase resetEventDateUseCase,
      ToggleEventReminderUseCase toggleEventReminderUseCase,
      DeleteEventUseCase deleteEventUseCase,
      CreateEventUseCase createEventUseCase) {

    super();

    this.eventViewModelMapper = eventViewModelMapper;
    this.eventBus = eventBus;
    this.getEventsUseCase = getEventsUseCase;
    this.getFilteredEventsUseCase = getFilteredEventsUseCase;
    this.favoriteEventUseCase = favoriteEventUseCase;
    this.resetEventDateUseCase = resetEventDateUseCase;
    this.toggleEventReminderUseCase = toggleEventReminderUseCase;
    this.deleteEventUseCase = deleteEventUseCase;
    this.createEventUseCase = createEventUseCase;
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
  }

  public void setFilterStrategy(EventFilterStrategy filterStrategy) {

    this.filterStrategy = filterStrategy;
  }

  private void getLocalEvents(final boolean pullToRefresh, boolean updateTheView) {

    final EventListView view = getView();
    final RequestValues requestValues = new RequestValues(filterStrategy, pullToRefresh);

    Disposable subscribe = getFilteredEventsUseCase.execute(requestValues)
        .map(eventViewModelMapper::fromEvent)
        .doOnSuccess(result -> {

          Timber.d("getLocalEvents: %d", result.size());

          unfilteredEvents = result;

          if (updateTheView) {
            view.setData(result);
            view.showContent();
          }
        })
        .doOnError(error -> view.onError(error.getMessage()))
        .subscribe();

    addDisposable(subscribe);
  }

  public void observeSearchQuery(Observable<String> observable) {

    Disposable subscribe = observable
        .debounce(SEARCH_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(filter -> {

          Timber.d("Filter: %s", filter);

          if (StringUtils.isNullOrEmpty(filter)) {
            removeFilter();

          } else {
            filterEventsByText(filter);
          }
        });

    addDisposable(subscribe);
  }

  private void filterEventsByText(String text) {

    Timber.d("filterEventsByText");

    final EventListView view = getView();

    Disposable subscribe = Single.just(unfilteredEvents)
        .flatMapObservable(Observable::fromIterable)
        .filter(event -> event.getName() != null)
        .filter(event -> filterEventByText(event, text))
        .toList()
        .subscribe(view::setData);

    addDisposable(subscribe);
  }

  private void removeFilter() {

    Timber.d("removeFilter");

    final EventListView view = getView();
    view.setData(unfilteredEvents);
    view.showContent();
  }

  private boolean filterEventByText(EventViewModel event, String text) {

    final String normalizedName = StringUtils.normalizeText(event.getName().toLowerCase());
    final String normalizedText = StringUtils.normalizeText(text.toLowerCase());

    return normalizedName.contains(normalizedText);
  }

  private void updateUnfilteredEvents() {

    getLocalEvents(false, false);
  }

  public void loadEvents(final boolean pullToRefresh) {

    if (pullToRefresh) {
      EventListView view = getView();

      Disposable subscribe = getEventsUseCase.execute(true)
          .subscribe(list -> {
            Timber.d("getEvents: %d", list.size());
            getLocalEvents(true, true);
          }, error -> view.onError(error.getMessage()));

      addDisposable(subscribe);
    } else {
      getLocalEvents(false, true);
    }
  }

  public void editEvent(@NonNull EventViewModel event) {

    getView().showEditEventUi(event);
  }

  public void deleteEvent(EventViewModel eventViewModel) {

    final EventListView view = getView();
    final Event event = eventViewModelMapper.toEvent(eventViewModel);

    Disposable subscribe = deleteEventUseCase.execute(event)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(deleted -> {
          if (deleted) {
            updateUnfilteredEvents();
          }
        })
        .doOnSuccess(deleted -> view.deleteSuccessfully(eventViewModel, deleted))
        .doOnError(error -> {
          Timber.e(error);
          //AirtableException airtableException = error;
          view.onError(error.getMessage());
        })
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  public void makeEventFavorite(@NonNull EventViewModel eventViewModel) {

    final EventListView view = getView();
    final Event event = eventViewModelMapper.toEvent(eventViewModel);

    Disposable subscribe = favoriteEventUseCase.execute(
        event)
        .map(eventViewModelMapper::fromEvent)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(result -> updateUnfilteredEvents())
        .doOnSuccess(view::favoriteSuccessfully)
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  public void undoDelete(@NonNull EventViewModel eventViewModel) {

    final EventListView view = getView();
    final Event event = eventViewModelMapper.toEvent(eventViewModel);

    Disposable subscribe = createEventUseCase.execute(event)
        .map(eventViewModelMapper::fromEvent)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(result -> updateUnfilteredEvents())
        .doOnSuccess(view::undoDeleteSuccessfully)
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  public void resetDate(EventViewModel eventViewModel) {

    final EventListView view = getView();
    final Event event = eventViewModelMapper.toEvent(eventViewModel);

    Disposable subscribe = resetEventDateUseCase.execute(event)
        .map(eventViewModelMapper::fromEvent)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(result -> updateUnfilteredEvents())
        .doOnSuccess(view::dateResetSuccessfully)
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  public void toggleEventReminder(EventViewModel eventViewModel) {

    final EventListView view = getView();
    final Event event = eventViewModelMapper.toEvent(eventViewModel);

    Disposable subscribe = toggleEventReminderUseCase.execute(event)
        .map(eventViewModelMapper::fromEvent)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(result -> updateUnfilteredEvents())
        .doOnSuccess(view::reminderSuccessfully)
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(EventCreatedEvent createdEvent) {

    EventListView view = getView();
    EventViewModel eventViewModel = createdEvent.event;
    Event event = eventViewModelMapper.toEvent(eventViewModel);

    if (filterStrategy.eventMatchFilter(event)) {
      view.showCreatedEvent(eventViewModel);
    }

    view.createSuccessfully(eventViewModel);
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

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(EventsUpdatedEvent event) {

    Timber.d("EventsUpdatedEvent received");

    getView().loadData(false);
  }

}
