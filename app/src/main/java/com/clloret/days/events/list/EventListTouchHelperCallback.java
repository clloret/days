package com.clloret.days.events.list;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.View;
import com.clloret.days.events.list.EventListAdapter.EventViewHolder;
import timber.log.Timber;

class EventListTouchHelperCallback extends Callback {

  private final EventListAdapter adapter;

  public EventListTouchHelperCallback(EventListAdapter adapter) {

    super();

    this.adapter = adapter;
  }

  @Override
  public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {

    int swipeFlags = ItemTouchHelper.START;

    return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, swipeFlags);
  }

  @Override
  public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {

    return false;
  }

  @Override
  public boolean isItemViewSwipeEnabled() {

    return true;
  }

  @Override
  public void onSwiped(ViewHolder viewHolder, int direction) {

    Timber.d("onSwiped: %d", direction);

    adapter.onItemRemove(viewHolder);
  }

  @Override
  public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {

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
  public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dx,
      float dy, int actionState, boolean isCurrentlyActive) {

    final View foregroundView = ((EventViewHolder) viewHolder).viewForeground;
    getDefaultUIUtil()
        .onDraw(c, recyclerView, foregroundView, dx, dy, actionState, isCurrentlyActive);
  }

  @Override
  public void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dx,
      float dy, int actionState, boolean isCurrentlyActive) {

    final View foregroundView = ((EventViewHolder) viewHolder).viewForeground;
    getDefaultUIUtil()
        .onDrawOver(c, recyclerView, foregroundView, dx, dy, actionState, isCurrentlyActive);
  }

}
