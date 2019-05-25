package com.clloret.days.events.create;

import static com.clloret.days.utils.FabProgressUtils.PROGRESS_DELAY;

import com.clloret.days.base.BaseRxPresenter;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.injection.TypeNamed;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.interactors.tags.GetTagsUseCase;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;

public class EventCreatePresenter extends BaseRxPresenter<EventCreateView> {

  private final EventViewModelMapper eventViewModelMapper;
  private final TagViewModelMapper tagViewModelMapper;
  private final GetTagsUseCase getTagsUseCase;
  private final CreateEventUseCase createEventUseCase;
  private final Scheduler uiThread;

  @Inject
  public EventCreatePresenter(
      EventViewModelMapper eventViewModelMapper,
      TagViewModelMapper tagViewModelMapper,
      GetTagsUseCase getTagsUseCase,
      CreateEventUseCase createEventUseCase,
      @Named(TypeNamed.UI_SCHEDULER) Scheduler uiThread) {

    super();

    this.eventViewModelMapper = eventViewModelMapper;
    this.tagViewModelMapper = tagViewModelMapper;
    this.getTagsUseCase = getTagsUseCase;
    this.createEventUseCase = createEventUseCase;
    this.uiThread = uiThread;
  }

  public void createEvent(EventViewModel eventViewModel) {

    EventCreateView view = getView();

    if (eventViewModel.getName().isEmpty()) {
      view.onEmptyEventNameError();
      return;
    }

    if (eventViewModel.getDate() == null) {
      view.onEmptyEventDateError();
      return;
    }

    Event event = eventViewModelMapper.toEvent(eventViewModel);

    Disposable subscribe = createEventUseCase.execute(event)
        .delay(PROGRESS_DELAY, TimeUnit.MILLISECONDS, uiThread)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .map(eventViewModelMapper::fromEvent)
        .doOnSuccess(result -> {
          view.showIndeterminateProgressFinalAnimation();
          view.onSuccessfully(result);
        })
        .doOnError(error -> {
          view.hideIndeterminateProgress();
          view.onError(error.getMessage());
        })
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  public void loadTags() {

    final EventCreateView view = getView();

    Disposable subscribe = getTagsUseCase.execute(false)
        .map(tagViewModelMapper::fromTag)
        .doOnSuccess(view::setData)
        .doOnError(view::showError)
        .subscribe();

    addDisposable(subscribe);
  }

}