package com.clloret.days.menu.items;

import com.clloret.days.menu.items.holders.TextViewHolder;

public abstract class DrawerText extends DrawerMenuItem<TextViewHolder> {

  String title;

  @SuppressWarnings("unused")
  protected DrawerText() {

    super();
  }

  public DrawerText(String title) {

    super();

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
