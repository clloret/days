package com.clloret.days.events.create;

import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

public interface EventCreateView extends MvpView {

  void onSuccessfully(EventViewModel event);

  void onError(String message);

  void setData(List<TagViewModel> data);

  void showError(Throwable t);

  void onEmptyEventNameError();

  void onEmptyEventDateError();
}
