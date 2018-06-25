package com.clloret.days.events.create;

import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.Tag;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

public interface EventCreateView extends MvpView {

  void onSuccessfully(Event event);

  void onError(String message);

  void setData(List<Tag> data);

  void showError(Throwable t);

  void onEmptyEventNameError();

  void onEmptyEventDateError();
}
