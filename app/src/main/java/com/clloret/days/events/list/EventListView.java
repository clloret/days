package com.clloret.days.events.list;

import com.clloret.days.model.entities.EventViewModel;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import java.util.List;

public interface EventListView extends MvpLceView<List<EventViewModel>> {

  // MvpLceView already defines LCE methods:
  //
  // void showLoading(boolean pullToRefresh)
  // void showError(Throwable t, boolean pullToRefresh)
  // void setData(List<Country> data)
  // void showContent()

  void onError(String message);

  void showMessage(String message);

  void showEditEventUi(EventViewModel event);

  void showCreatedEvent(EventViewModel event);

  void deleteSuccessfully(EventViewModel event, boolean deleted);

  void undoDeleteSuccessfully(EventViewModel event);

  void updateSuccessfully(EventViewModel event);

  void favoriteSuccessfully(EventViewModel event);

  void dateResetSuccessfully(EventViewModel event);
}
