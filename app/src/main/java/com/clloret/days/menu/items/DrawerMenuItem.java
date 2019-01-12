package com.clloret.days.menu.items;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.clloret.days.menu.items.holders.BaseViewHolder;

public abstract class DrawerMenuItem<T extends BaseViewHolder> {

  T viewHolder;

  public abstract View getView(LayoutInflater inflater, ViewGroup parent);

  public abstract void setView(View view);

  public void populate() {

  }

  public boolean isSelectable() {

    return false;
  }

  public boolean isClickable() {

    return true;
  }

  public void select(FragmentActivity activity) {

  }

  public void deselect() {

  }

  public abstract int getType();

  abstract T createViewHolder();

  void configureViewHolder(View view) {

    if (viewHolder == null) {
      viewHolder = createViewHolder();
    }
    viewHolder.item = this;
    viewHolder.view = view;
  }

}
