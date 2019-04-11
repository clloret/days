package com.clloret.days.events.create;

import static com.clloret.days.utils.FabProgressUtils.PROGRESS_DELAY;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.interactors.tags.GetTagsUseCase;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class EventCreatePresenter extends MvpNullObjectBasePresenter<EventCreateView> {

  private final EventViewModelMapper eventViewModelMapper;
  private final TagViewModelMapper tagViewModelMapper;
  private final GetTagsUseCase getTagsUseCase;
  private final CreateEventUseCase createEventUseCase;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public EventCreatePresenter(
      EventViewModelMapper eventViewModelMapper,
      TagViewModelMapper tagViewModelMapper,
      GetTagsUseCase getTagsUseCase,
      CreateEventUseCase createEventUseCase) {

    this.eventViewModelMapper = eventViewModelMapper;
    this.tagViewModelMapper = tagViewModelMapper;
    this.getTagsUseCase = getTagsUseCase;
    this.createEventUseCase = createEventUseCase;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
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
        .subscribeOn(Schedulers.io())
        .delay(PROGRESS_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
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

    disposable.add(subscribe);
  }

  public void loadTags() {

    final EventCreateView view = getView();

    Disposable subscribe = getTagsUseCase.execute(false)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(tagViewModelMapper::fromTag)
        .doOnSuccess(view::setData)
        .doOnError(view::showError)
        .subscribe();

    disposable.add(subscribe);
  }

}