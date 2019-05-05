package com.clloret.days.menu.items;

import com.clloret.days.utils.Optional;

public class DrawerTagSelectedMgr {

  private Optional<DrawerTag> selected;

  void select(DrawerTag drawerTag) {

    selected = Optional.of(drawerTag);
  }

  void deselect() {

    selected = Optional.empty();
  }

  public Optional<DrawerTag> getSelected() {

    return selected;
  }
}
