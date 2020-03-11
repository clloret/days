package com.clloret.days.menu.items;

import androidx.annotation.NonNull;
import com.clloret.days.menu.items.holders.TextViewHolder;

public abstract class DrawerText extends DrawerMenuItem<TextViewHolder> {

  public String title;

  @SuppressWarnings("unused")
  protected DrawerText() {

    super();
  }

  public DrawerText(String title) {

    super();

    this.title = title;
  }

  @Override
  public void populate() {

    if (viewHolder.name != null) {
      viewHolder.name.setText(title);
    }
  }

  @Override
  TextViewHolder createViewHolder() {

    return new TextViewHolder();
  }

  @NonNull
  @Override
  public String toString() {

    return title;
  }
}
