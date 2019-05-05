package com.clloret.days.tags.create;

import android.support.annotation.NonNull;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.tags.CreateTagUseCase;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagCreatePresenter extends MvpNullObjectBasePresenter<TagCreateView> {

  private final CreateTagUseCase createTagUseCase;
  private final TagViewModelMapper tagViewModelMapper;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public TagCreatePresenter(TagViewModelMapper tagViewModelMapper,
      CreateTagUseCase createTagUseCase) {

    super();

    this.tagViewModelMapper = tagViewModelMapper;
    this.createTagUseCase = createTagUseCase;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void createTag(@NonNull String name) {

    if (name.isEmpty()) {
      getView().onEmptyTagNameError();
      return;
    }

    TagCreateView view = getView();
    Tag newTag = new Tag(null, name);

    Disposable subscribe = createTagUseCase.execute(newTag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(result -> view.onSuccessfully(tagViewModelMapper.fromTag(result)))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    disposable.add(subscribe);
  }
}