package com.clloret.days.events.list;

import android.graphics.Canvas;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.clloret.days.events.list.EventListAdapter.EventViewHolder;
import timber.log.Timber;

class EventListTouchHelperCallback extends Callback {

  private final EventListAdapter adapter;

  public EventListTouchHelperCallback(EventListAdapter adapter) {

    super();

    this.adapter = adapter;
  }

  @Override
  public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {

    int swipeFlags = ItemTouchHelper.START;

    return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, swipeFlags);
  }

  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder,
      @NonNull ViewHolder target) {

    return false;
  }

  @Override
  public boolean isItemViewSwipeEnabled() {

    return true;
  }

  @Override
  public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {

    Timber.d("onSwiped: %d", direction);

    adapter.onItemRemove(viewHolder);
  }

  @Override
  public void clearView(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {

    final View foregroundView = ((EventViewHolder) viewHolder).viewForeground;
    getDefaultUIUtil().clearView(foregroundView);
  }

  @Override
  public void onSelectedChanged(ViewHolder viewHolder, int actionState) {

    if (viewHolder != null) {
      final View foregroundView = ((EventViewHolder) viewHolder).viewForeground;
      getDefaultUIUtil().onSelected(foregroundView);
    }
  }

  @Override
  public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
      @NonNull ViewHolder viewHolder, float dx,
      float dy, int actionState, boolean isCurrentlyActive) {

    final View foregroundView = ((EventViewHolder) viewHolder).viewForeground;
    getDefaultUIUtil()
        .onDraw(c, recyclerView, foregroundView, dx, dy, actionState, isCurrentlyActive);
  }

  @Override
  public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
      ViewHolder viewHolder, float dx,
      float dy, int actionState, boolean isCurrentlyActive) {

    final View foregroundView = ((EventViewHolder) viewHolder).viewForeground;
    getDefaultUIUtil()
        .onDrawOver(c, recyclerView, foregroundView, dx, dy, actionState, isCurrentlyActive);
  }

}
