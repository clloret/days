package com.clloret.days.tags.edit;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

public class TagEditPresenter extends MvpNullObjectBasePresenter<TagEditView> {

  private final AppDataStore api;
  private final TagViewModelMapper tagViewModelMapper;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public TagEditPresenter(AppDataStore api, TagViewModelMapper tagViewModelMapper) {

    this.api = api;
    this.tagViewModelMapper = tagViewModelMapper;
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    disposable.dispose();
  }

  public void saveTag(TagViewModel tag) {

    TagEditView view = getView();

    if (tag.getName().isEmpty()) {

      getView().onEmptyTagNameError();
      return;
    }

    Disposable subscribe = api.editTag(tagViewModelMapper.toTag(tag))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(result -> view.onSuccessfully(tagViewModelMapper.fromTag(result)))
        .doOnError(error -> view.onError(error.getMessage()))
        .subscribe();
    disposable.add(subscribe);
  }

  public void deleteTag(TagViewModel tag) {

    TagEditView view = getView();

    Disposable subscribe = api.deleteTag(tagViewModelMapper.toTag(tag))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess(deleted -> view.deleteSuccessfully(tag, deleted))
        .doOnError(error -> view.onError(error.getMessage()))
        .subscribe();
    disposable.add(subscribe);
  }
}