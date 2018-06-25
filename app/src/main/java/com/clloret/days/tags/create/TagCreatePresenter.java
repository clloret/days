package com.clloret.days.tags.create;

import android.support.annotation.NonNull;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Tag;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagCreatePresenter extends MvpBasePresenter<TagCreateView> {

  private final AppDataStore api;
  private final CompositeDisposable disposable = new CompositeDisposable();

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
        .subscribe(result -> getView().onSuccessfully(result),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }
}