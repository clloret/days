package com.clloret.days.menu.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.clloret.days.R;
import com.clloret.days.menu.items.holders.BaseViewHolder;

public class DrawerSeparator extends DrawerMenuItem<BaseViewHolder> {

  @Override
  public View getView(LayoutInflater inflater, ViewGroup parent) {

    View convertView = inflater.inflate(R.layout.menu_adapter_separator, parent, false);

    setView(convertView);

    return convertView;
  }

  @Override
  public void setView(View view) {

    configureViewHolder(view);
  }

  @Override
  public boolean isClickable() {

    return false;
  }

  @Override
  public int getType() {

    return 0;
  }

  @Override
  public BaseViewHolder createViewHolder() {

    return new BaseViewHolder();
  }
}
