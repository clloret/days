package com.clloret.days.menu.items;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.DrawableRes;
import androidx.fragment.app.FragmentActivity;
import com.clloret.days.R;

public class DrawerAction extends DrawerTextIcon {

  private Intent intent;
  private int requestCode;

  @SuppressWarnings("unused")
  private DrawerAction() {

    super();
  }

  public DrawerAction(String title, @DrawableRes int icon, Intent intent, int requestCode) {

    super(title, icon);

    this.intent = intent;
    this.requestCode = requestCode;
  }

  @Override
  public View getView(LayoutInflater inflater, ViewGroup parent) {

    View convertView = inflater.inflate(R.layout.menu_adapter_texticon, parent, false);

    setView(convertView);

    return convertView;
  }

  @Override
  public void setView(View view) {

    configureViewHolder(view);
    viewHolder.icon = view.findViewById(R.id.imageview_texticon_icon);
    viewHolder.name = view.findViewById(R.id.textview_texticon_name);
  }

  @Override
  public void select(FragmentActivity activity) {

    if (requestCode > 0) {
      activity.startActivityForResult(intent, requestCode);
    } else {
      activity.startActivity(intent);
    }
  }

  @Override
  public int getType() {

    return 2;
  }
}
