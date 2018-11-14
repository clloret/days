package com.clloret.days.events.edit;

import com.clloret.days.device.events.ReminderScheduleEvent;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Objects;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;

public class EventEditPresenter extends MvpNullObjectBasePresenter<EventEditView> {

  private final AppDataStore api;
  private final EventBus eventBus;
  private final EventViewModelMapper eventViewModelMapper;
  private final TagViewModelMapper tagViewModelMapper;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public EventEditPresenter(AppDataStore api, EventViewModelMapper eventViewModelMapper,
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

  public void saveEvent(EventViewModel modifiedEvent, EventViewModel originalEvent) {

    EventEditView view = getView();

    if (modifiedEvent.getName().isEmpty()) {
      view.onEmptyEventNameError();
      return;
    }

    Disposable subscribe = api.editEvent(eventViewModelMapper.toEvent(modifiedEvent))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(result -> {

          boolean scheduleReminder = false;
          if (!Objects.equals(modifiedEvent.getReminder(), originalEvent.getReminder())) {
            scheduleReminder = true;
          }

          if (modifiedEvent.hasReminder() && (modifiedEvent.getDate() != originalEvent.getDate())) {
            scheduleReminder = true;
          }

          if (scheduleReminder) {
            eventBus.post(
                new ReminderScheduleEvent(result, modifiedEvent.hasReminder(),
                    originalEvent.hasReminder()));
          }

          view.onSuccessfully(eventViewModelMapper.fromEvent(result));
        })
        .doOnError(error -> view.onError(error.getMessage()))
        .subscribe();
    disposable.add(subscribe);
  }

  public void deleteEvent(EventViewModel event) {

    EventEditView view = getView();

    Event eventToDelete = eventViewModelMapper.toEvent(event);
    Disposable subscribe = api.deleteEvent(eventToDelete)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(deleted -> {

          if (event.hasReminder()) {
            eventBus.post(new ReminderScheduleEvent(eventToDelete, false, false));
          }

          view.deleteSuccessfully(event, deleted);
        })
        .doOnError(error -> view.onError(error.getMessage()))
        .subscribe();
    disposable.add(subscribe);
  }

  public void loadTags() {

    EventEditView view = getView();

    Disposable subscribe = api.getTags(false)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(tags -> view.setData(tagViewModelMapper.fromTag(tags)))
        .doOnError(view::showError)
        .subscribe();
    disposable.add(subscribe);
  }

}