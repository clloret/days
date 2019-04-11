package com.clloret.days.tags.edit;

import com.clloret.days.model.entities.TagViewModel;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface TagEditView extends MvpView {

  void onSuccessfully(TagViewModel tag);

  void deleteSuccessfully(TagViewModel tag, boolean deleted);

  void onError(String message);

  void onEmptyTagNameError();

  void showIndeterminateProgress();

  void hideIndeterminateProgress();

}
