package com.clloret.days.tags.edit;

import com.clloret.days.model.entities.Tag;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface TagEditView extends MvpView {

  void onSuccessfully(Tag tag);

  void deleteSuccessfully(Tag tag, boolean deleted);

  void onError(String message);

  void onEmptyTagNameError();
}
