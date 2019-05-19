package com.clloret.days.tags.edit;

import com.clloret.days.base.BaseRxPresenter;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.tags.DeleteTagUseCase;
import com.clloret.days.domain.interactors.tags.EditTagUseCase;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagEditPresenter extends BaseRxPresenter<TagEditView> {

  private final EditTagUseCase editTagUseCase;
  private final DeleteTagUseCase deleteTagUseCase;
  private final TagViewModelMapper tagViewModelMapper;

  @Inject
  public TagEditPresenter(EditTagUseCase editTagUseCase,
      DeleteTagUseCase deleteTagUseCase,
      TagViewModelMapper tagViewModelMapper) {

    super();

    this.editTagUseCase = editTagUseCase;
    this.deleteTagUseCase = deleteTagUseCase;
    this.tagViewModelMapper = tagViewModelMapper;
  }

  public void saveTag(TagViewModel tagViewModel) {

    if (tagViewModel.getName().isEmpty()) {

      getView().onEmptyTagNameError();
      return;
    }

    TagEditView view = getView();
    Tag tag = tagViewModelMapper.toTag(tagViewModel);

    Disposable subscribe = editTagUseCase.execute(tag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(tagViewModelMapper::fromTag)
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
    final Tag tag = tagViewModelMapper.toTag(tagViewModel);

    Disposable subscribe = deleteTagUseCase.execute(tag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(deleted -> view.deleteSuccessfully(tagViewModel, deleted))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }
}