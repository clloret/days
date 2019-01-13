package com.clloret.days.tags.create;

import android.support.annotation.NonNull;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagCreatePresenter extends MvpNullObjectBasePresenter<TagCreateView> {

  private final AppDataStore api;
  private final TagViewModelMapper tagViewModelMapper;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public TagCreatePresenter(AppDataStore api, TagViewModelMapper tagViewModelMapper) {

    this.api = api;
    this.tagViewModelMapper = tagViewModelMapper;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void createTag(@NonNull String name) {

    TagCreateView view = getView();

    if (name.isEmpty()) {
      getView().onEmptyTagNameError();
      return;
    }

    Tag newTag = new Tag(null, name);

    Disposable subscribe = api.createTag(newTag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(result -> view.onSuccessfully(tagViewModelMapper.fromTag(result)))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();
    disposable.add(subscribe);
  }
}