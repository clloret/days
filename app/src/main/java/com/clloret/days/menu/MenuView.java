package com.clloret.days.menu;

import com.clloret.days.model.entities.TagViewModel;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

public interface MenuView extends MvpView {

  void setData(List<TagViewModel> data);

  void showContent();

  void showError(Throwable t);

  void showCreatedTag(TagViewModel tag);

  void showEditTagUi(TagViewModel tag);

  void updateSuccessfully(TagViewModel tag);

  void deleteSuccessfully(TagViewModel tag);
}
