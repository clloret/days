package com.clloret.days.tags.edit;

import com.clloret.days.base.BaseRxPresenter;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.tags.DeleteTagUseCase;
import com.clloret.days.domain.interactors.tags.EditTagUseCase;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.entities.mapper.TagViewModelMapperKt;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class TagEditPresenter extends BaseRxPresenter<TagEditView> {

  private final EditTagUseCase editTagUseCase;
  private final DeleteTagUseCase deleteTagUseCase;

  @Inject
  public TagEditPresenter(
      EditTagUseCase editTagUseCase,
      DeleteTagUseCase deleteTagUseCase) {

    super();

    this.editTagUseCase = editTagUseCase;
    this.deleteTagUseCase = deleteTagUseCase;
  }

  public void saveTag(TagViewModel tagViewModel) {

    if (tagViewModel.getName().isEmpty()) {

      getView().onEmptyTagNameError();
      return;
    }

    final TagEditView view = getView();
    final Tag tag = TagViewModelMapperKt.toTag(tagViewModel);

    Disposable subscribe = editTagUseCase.execute(tag)
        .map(TagViewModelMapperKt::toTagViewModel)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(view::onSuccessfully)
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }

  public void deleteTag(TagViewModel tagViewModel) {

    final TagEditView view = getView();
    final Tag tag = TagViewModelMapperKt.toTag(tagViewModel);

    Disposable subscribe = deleteTagUseCase.execute(tag)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(deleted -> view.deleteSuccessfully(tagViewModel, deleted))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }
}