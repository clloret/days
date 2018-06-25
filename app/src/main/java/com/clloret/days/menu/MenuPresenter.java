package com.clloret.days.menu;

import android.support.annotation.NonNull;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Tag;
import com.clloret.days.model.events.RefreshRequestEvent;
import com.clloret.days.model.events.TagCreatedEvent;
import com.clloret.days.model.events.TagDeletedEvent;
import com.clloret.days.model.events.TagModifiedEvent;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MenuPresenter extends MvpBasePresenter<MenuView> {

  private final AppDataStore api;
  private final EventBus eventBus;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public MenuPresenter(AppDataStore api, EventBus eventBus) {

    this.api = api;
    this.eventBus = eventBus;
  }

  @Override
  public void attachView(MenuView view) {

    super.attachView(view);
    eventBus.register(this);
  }

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    eventBus.unregister(this);
    disposable.dispose();
  }

  public void loadTags(final boolean pullToRefresh) {

    Disposable subscribe = api.getTags(pullToRefresh)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tags -> {
          if (isViewAttached()) {
            getView().setData(tags);
            getView().showContent();
          }
        }, error -> {
          if (isViewAttached()) {
            getView().showError(error);
          }
        });
    disposable.add(subscribe);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(TagCreatedEvent event) {

    getView().showCreatedTag(event.tag);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(TagModifiedEvent event) {

    getView().updateSuccessfully(event.tag);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(TagDeletedEvent event) {

    getView().deleteSuccessfully(event.tag);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEvent(RefreshRequestEvent event) {

    loadTags(true);
  }

  public void editTag(@NonNull Tag tag) {

    getView().showEditTagUi(tag);
  }

}
