package com.clloret.days.menu.items;

import android.support.annotation.DrawableRes;

public abstract class DrawerTextIcon extends DrawerText {

  private int icon;

  @SuppressWarnings("unused")
  protected DrawerTextIcon() {

  }

  public DrawerTextIcon(String listingTitle, @DrawableRes int icon) {

    this.title = listingTitle;
    this.icon = icon;
  }

  @Override
  public void populate() {

    super.populate();

    if (viewHolder.icon != null) {
      viewHolder.icon.setImageResource(icon);
    }
  }
}
