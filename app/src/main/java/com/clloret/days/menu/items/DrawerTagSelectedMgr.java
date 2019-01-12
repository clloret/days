package com.clloret.days.menu.items;

import android.support.annotation.Nullable;

public class DrawerTagSelectedMgr {

  private DrawerTag selected;

  void select(DrawerTag drawerTag) {

    selected = drawerTag;
  }

  void deselect() {

    selected = null;
  }

  public @Nullable
  DrawerTag getSelected() {

    return selected;
  }
}
