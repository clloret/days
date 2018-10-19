package com.clloret.days.tags.create;

import com.clloret.days.model.entities.TagViewModel;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface TagCreateView extends MvpView {

  void onSuccessfully(TagViewModel tag);

  void onError(String message);

  void onEmptyTagNameError();
}
