package com.clloret.days.events.create;

import com.clloret.days.device.events.ReminderScheduleEvent;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;

public class EventCreatePresenter extends MvpNullObjectBasePresenter<EventCreateView> {

  private final AppDataStore api;
  private final EventBus eventBus;
  private final EventViewModelMapper eventViewModelMapper;
  private final TagViewModelMapper tagViewModelMapper;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public EventCreatePresenter(AppDataStore api, EventViewModelMapper eventViewModelMapper,
      TagViewModelMapper tagViewModelMapper, EventBus eventBus) {

    this.api = api;
    this.eventViewModelMapper = eventViewModelMapper;
    this.tagViewModelMapper = tagViewModelMapper;
    this.eventBus = eventBus;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void createEvent(EventViewModel event) {

    EventCreateView view = getView();

    if (event.getName().isEmpty()) {
      view.onEmptyEventNameError();
      return;
    }

    if (event.getDate() == null) {
      view.onEmptyEventDateError();
      return;
    }

    Disposable subscribe = api.createEvent(eventViewModelMapper.toEvent(event))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(result -> {
          EventViewModel eventViewModel = eventViewModelMapper.fromEvent(result);
          if (eventViewModel.hasReminder()) {
            eventBus.post(new ReminderScheduleEvent(result, true, false));
          }

          view.onSuccessfully(eventViewModel);
        })
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    disposable.add(subscribe);
  }

  public void loadTags() {

    EventCreateView view = getView();

    Disposable subscribe = api.getTags(false)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(tags -> view.setData(tagViewModelMapper.fromTag(tags)))
        .doOnError(view::showError)
        .subscribe();
    disposable.add(subscribe);
  }

}