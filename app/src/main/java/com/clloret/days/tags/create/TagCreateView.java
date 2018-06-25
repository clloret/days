package com.clloret.days.tags.create;

import com.clloret.days.model.entities.Tag;
import com.hannesdorfmann.mosby.mvp.MvpView;

public interface TagCreateView extends MvpView {

  void onSuccessfully(Tag tag);

  void onError(String message);

  void onEmptyTagNameError();
}
