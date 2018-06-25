package com.clloret.days.menu.items;

import com.clloret.days.menu.items.holders.TextViewHolder;

public abstract class DrawerText extends DrawerMenuItem<TextViewHolder> {

  String title = null;

  @SuppressWarnings("unused")
  protected DrawerText() {

  }

  public DrawerText(String title) {

    this.title = title;
  }

  @Override
  TextViewHolder createViewHolder() {

    return new TextViewHolder();
  }

  @Override
  public void populate() {

    if (viewHolder.name != null) {
      viewHolder.name.setText(title);
    }
  }
}
