package com.clloret.days.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

@SuppressWarnings("unused")
public class ScrollAwareFabBehavior extends FloatingActionButton.Behavior {

  public ScrollAwareFabBehavior(Context context, AttributeSet attrs) {

    super();
  }

  @Override
  public boolean onStartNestedScroll(@NonNull final CoordinatorLayout coordinatorLayout,
      @NonNull final FloatingActionButton child, @NonNull final View directTargetChild,
      @NonNull final View target, final int nestedScrollAxes, int type) {

    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
  }

  @Override
  public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
      @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed,
      int dxUnconsumed, int dyUnconsumed, int type) {

    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed, type);

    if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
      child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
        @Override
        public void onHidden(FloatingActionButton fab) {

          super.onHidden(fab);
          child.setVisibility(View.INVISIBLE);
        }
      });
    } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
      child.show();
    }
  }
}
