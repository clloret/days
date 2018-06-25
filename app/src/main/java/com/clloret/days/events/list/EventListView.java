package com.clloret.days.events.list;

import com.clloret.days.model.entities.Event;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import java.util.List;

public interface EventListView extends MvpLceView<List<Event>> {

  // MvpLceView already defines LCE methods:
  //
  // void showLoading(boolean pullToRefresh)
  // void showError(Throwable t, boolean pullToRefresh)
  // void setData(List<Country> data)
  // void showContent()

  void onError(String message);

  void showMessage(String message);

  void showEditEventUi(Event event);

  void showCreatedEvent(Event event);

  void deleteSuccessfully(Event event, boolean deleted);

  void undoDeleteSuccessfully(Event event);

  void updateSuccessfully(Event event);

  void favoriteSuccessfully(Event event);

  void dateResetSuccessfully(Event event);
}
