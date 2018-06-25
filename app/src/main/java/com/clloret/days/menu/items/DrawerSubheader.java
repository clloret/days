package com.clloret.days.menu.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.clloret.days.R;

public class DrawerSubheader extends DrawerText {

  public DrawerSubheader(String title) {

    super(title);
  }

  @Override
  public View getView(LayoutInflater inflater, ViewGroup parent) {

    View convertView = inflater.inflate(R.layout.menu_adapter_subheader, parent, false);

    setView(convertView);

    return convertView;
  }

  @Override
  public void setView(View view) {

    configureViewHolder(view);
    viewHolder.name = view.findViewById(R.id.textview_subheader_name);
  }

  @Override
  public int getType() {

    return 1;
  }

  @Override
  public boolean isClickable() {

    return false;
  }
}
