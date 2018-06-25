package com.clloret.days.events.edit;

import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.Tag;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

public interface EventEditView extends MvpView {

  void onSuccessfully(Event event);

  void onError(String message);

  void setData(List<Tag> data);

  void showError(Throwable t);

  void onEmptyEventNameError();

  void deleteSuccessfully(Event event, boolean deleted);
}
