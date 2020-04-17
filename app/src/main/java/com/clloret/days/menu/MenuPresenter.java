package com.clloret.days.menu;

import androidx.annotation.NonNull;
import com.clloret.days.base.BaseRxPresenter;
import com.clloret.days.device.eventbus.RefreshRequestEvent;
import com.clloret.days.domain.interactors.tags.GetTagsUseCase;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.entities.mapper.TagViewModelMapperKt;
import com.clloret.days.model.events.TagCreatedEvent;
import com.clloret.days.model.events.TagDeletedEvent;
import com.clloret.days.model.events.TagModifiedEvent;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MenuPresenter extends BaseRxPresenter<MenuView> {

  private final EventBus eventBus;

  @Inject
  GetTagsUseCase getTagsUseCase;

  @Inject
  public MenuPresenter(EventBus eventBus) {

    super();

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
  }

  public void loadTags(final boolean pullToRefresh) {

    final MenuView view = getView();

    Disposable subscribe = getTagsUseCase.execute(pullToRefresh)
        .map(TagViewModelMapperKt::toTagViewModelList)
        .subscribe(view::showTags, view::showError);

    addDisposable(subscribe);
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
