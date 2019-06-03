package com.clloret.days.menu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.clloret.days.model.entities.TagViewModel;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import java.util.ArrayList;
import java.util.List;

class MenuViewState implements RestorableViewState<MenuView> {

  private static final String KEY_DATA = "data";

  private List<TagViewModel> tags = new ArrayList<>();

  @Override
  public void apply(MenuView view, boolean retained) {

    view.setData(tags);
  }

  @Override
  public void saveInstanceState(@NonNull Bundle out) {

    out.putParcelableArrayList(KEY_DATA, new ArrayList<>(tags));
  }

  @Override
  public RestorableViewState<MenuView> restoreInstanceState(Bundle in) {

    if (in == null) {
      return null;
    }

    tags = in.getParcelableArrayList(KEY_DATA);

    return this;
  }

  public void setTags(List<TagViewModel> data) {

    tags.clear();
    tags.addAll(data);
  }
}
