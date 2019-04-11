package com.clloret.days.events.edit;

import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;

public interface EventEditView extends MvpView {

  void onSuccessfully(EventViewModel event);

  void onError(String message);

  void setData(List<TagViewModel> data);

  void showError(Throwable t);

  void showIndeterminateProgress();

  void showIndeterminateProgressFinalAnimation();

  void hideIndeterminateProgress();

  void onEmptyEventNameError();

  void deleteSuccessfully(EventViewModel event, boolean deleted);
}
