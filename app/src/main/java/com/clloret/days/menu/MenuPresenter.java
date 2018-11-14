package com.clloret.days.menu;

import android.support.annotation.NonNull;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.clloret.days.model.events.RefreshRequestEvent;
import com.clloret.days.model.events.TagCreatedEvent;
import com.clloret.days.model.events.TagDeletedEvent;
import com.clloret.days.model.events.TagModifiedEvent;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MenuPresenter extends MvpNullObjectBasePresenter<MenuView> {

  private final AppDataStore api;
  private final EventBus eventBus;
  private final TagViewModelMapper tagViewModelMapper;
  private final CompositeDisposable disposable = new CompositeDisposable();

  @Inject
  public MenuPresenter(AppDataStore api, TagViewModelMapper tagViewModelMapper, EventBus eventBus) {

    this.api = api;
    this.tagViewModelMapper = tagViewModelMapper;
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

    MenuView view = getView();

    Disposable subscribe = api.getTags(pullToRefresh)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tags -> {
          view.setData(tagViewModelMapper.fromTag(tags));
          view.showContent();
        }, view::showError);
    disposable.add(subscribe);
  }

  public void editTag(@NonNull TagViewModel tag) {

    getView().showEditTagUi(tag);
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

}
