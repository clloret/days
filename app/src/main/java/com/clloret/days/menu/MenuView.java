package com.clloret.days.menu;

import com.clloret.days.model.entities.Tag;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

public interface MenuView extends MvpView {

  void setData(List<Tag> data);

  void showContent();

  void showError(Throwable t);

  void showCreatedTag(Tag tag);

  void showEditTagUi(Tag tag);

  void updateSuccessfully(Tag tag);

  void deleteSuccessfully(Tag tag);
}
