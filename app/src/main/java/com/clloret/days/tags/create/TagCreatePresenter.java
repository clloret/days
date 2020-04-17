package com.clloret.days.tags.create;

import androidx.annotation.NonNull;
import com.clloret.days.base.BaseRxPresenter;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.tags.CreateTagUseCase;
import com.clloret.days.model.entities.mapper.TagViewModelMapperKt;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class TagCreatePresenter extends BaseRxPresenter<TagCreateView> {

  private final CreateTagUseCase createTagUseCase;

  @Inject
  public TagCreatePresenter(CreateTagUseCase createTagUseCase) {

    super();

    this.createTagUseCase = createTagUseCase;
  }

  public void createTag(@NonNull String name) {

    if (name.isEmpty()) {
      getView().onEmptyTagNameError();
      return;
    }

    TagCreateView view = getView();
    Tag newTag = new Tag(null, name);

    Disposable subscribe = createTagUseCase.execute(newTag)
        .doOnSubscribe(disposable -> view.showIndeterminateProgress())
        .doFinally(view::hideIndeterminateProgress)
        .doOnSuccess(result -> view.onSuccessfully(TagViewModelMapperKt.toTagViewModel(result)))
        .doOnError(error -> view.onError(error.getMessage()))
        .onErrorComplete()
        .subscribe();

    addDisposable(subscribe);
  }
}