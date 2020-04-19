package com.clloret.days.events.edit;

import static com.clloret.days.utils.FabProgressUtils.PROGRESS_DELAY;

import com.clloret.days.base.BaseRxPresenter;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.injection.TypeNamed;
import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.EditEventUseCase;
import com.clloret.days.domain.interactors.events.EditEventUseCase.RequestValues;
import com.clloret.days.domain.interactors.tags.GetTagsUseCase;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapperKt;
import com.clloret.days.model.entities.mapper.TagViewModelMapperKt;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Named;

public class EventEditPresenter extends BaseRxPresenter<EventEditView> {

  private final GetTagsUseCase getTagsUseCase;
  private final EditEventUseCase editEventUseCase;
  private final DeleteEventUseCase deleteEventUseCase;
  private final Scheduler uiThread;

  @Inject
  public EventEditPresenter(
      GetTagsUseCase getTagsUseCase,
      EditEventUseCase editEventUseCase,
      DeleteEventUseCase deleteEventUseCase,
      @Named(TypeNamed.UI_SCHEDULER) Scheduler uiThread) {

    super();

    this.getTagsUseCase = getTagsUseCase;
    this.editEventUseCase = editEventUseCase;
    this.deleteEventUseCase = deleteEventUseCase;
    this.uiThread = uiThread;
  }

  public void saveEvent(EventViewModel modifiedEventViewModel,
      EventViewModel originalEventViewModel) {

    final EventEditView view = getView();

    if (modifiedEventViewModel.getName().isEmpty()) {
      view.onEmptyEventNameError();
      return;
    }

    final Event modifiedEvent = EventViewModelMapperKt.toEvent(modifiedEventViewModel);
    final Event originalEvent = EventViewModelMapperKt.toEvent(originalEventViewModel);
    final RequestValues requestValues = new RequestValues(modifiedEvent, originalEvent);

    Disposable subscribe = editEventUseCase.execute(
        requestValues)
        .delay(PROGRESS_DELAY, TimeUnit.MILLISECONDS, uiThread)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .map(EventViewModelMapperKt::toEventViewModel)
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

  public void deleteEvent(EventViewModel eventViewModel) {

    final EventEditView view = getView();
    final Event event = EventViewModelMapperKt.toEvent(eventViewModel);

    Disposable subscribe = deleteEventUseCase.execute(event)
        .doOnSuccess(deleted -> view.deleteSuccessfully(eventViewModel, deleted))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  public void loadTags() {

    final EventEditView view = getView();

    Disposable subscribe = getTagsUseCase.execute(false)
        .map(TagViewModelMapperKt::toTagViewModelList)
        .doOnSuccess(view::setData)
        .doOnError(view::showError)
        .subscribe();

    addDisposable(subscribe);
  }

}