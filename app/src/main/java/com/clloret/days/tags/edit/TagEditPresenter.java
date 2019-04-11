package com.clloret.days.tags.edit;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.tags.DeleteTagUseCase;
import com.clloret.days.domain.interactors.tags.EditTagUseCase;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagEditPresenter extends MvpNullObjectBasePresenter<TagEditView> {

  private final EditTagUseCase editTagUseCase;
  private final DeleteTagUseCase deleteTagUseCase;
  private final TagViewModelMapper tagViewModelMapper;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public TagEditPresenter(EditTagUseCase editTagUseCase,
      DeleteTagUseCase deleteTagUseCase,
      TagViewModelMapper tagViewModelMapper) {

    this.editTagUseCase = editTagUseCase;
    this.deleteTagUseCase = deleteTagUseCase;
    this.tagViewModelMapper = tagViewModelMapper;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void saveTag(TagViewModel tagViewModel) {

    final TagEditView view = getView();

    if (tagViewModel.getName().isEmpty()) {

      getView().onEmptyTagNameError();
      return;
    }

    final Tag tag = tagViewModelMapper.toTag(tagViewModel);

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

    disposable.add(subscribe);
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

    disposable.add(subscribe);
  }
}