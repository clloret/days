package com.clloret.days.tags.create;

import android.support.annotation.NonNull;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagCreatePresenter extends MvpBasePresenter<TagCreateView> {

  private final AppDataStore api;
  private final CompositeDisposable disposable = new CompositeDisposable();
  private TagViewModelMapper tagViewModelMapper = new TagViewModelMapper();

  @Inject
  public TagCreatePresenter(AppDataStore api) {

    this.api = api;
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

    Tag newTag = new Tag(null, name);

    Disposable subscribe = api.createTag(newTag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> getView().onSuccessfully(tagViewModelMapper.fromTag(result)),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }
}