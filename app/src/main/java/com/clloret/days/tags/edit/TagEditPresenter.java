package com.clloret.days.tags.edit;

import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Tag;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagEditPresenter extends MvpBasePresenter<TagEditView> {

  private final AppDataStore api;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public TagEditPresenter(AppDataStore api) {

    this.api = api;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void saveTag(Tag tag) {

    if (tag.getName().isEmpty()) {

      getView().onEmptyTagNameError();
      return;
    }

    Disposable subscribe = api.editTag(tag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> getView().onSuccessfully(result),
            error -> getView().onError(error.getMessage()));
    disposable.add(subscribe);
  }

  public void deleteTag(Tag tag) {

    Disposable subscribe = api.deleteTag(tag)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(deleted -> getView().deleteSuccessfully(tag, deleted), error -> {
          if (isViewAttached()) {
            getView().onError(error.getMessage());
          }
        });
    disposable.add(subscribe);
  }
}