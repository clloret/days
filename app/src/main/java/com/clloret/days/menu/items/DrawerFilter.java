package com.clloret.days.menu.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.clloret.days.R;
import com.clloret.days.domain.events.filter.EventFilterStrategy;
import com.clloret.days.events.list.EventListFragment;

public class DrawerFilter extends DrawerTextIcon {

  protected EventFilterStrategy eventFilterStrategy;

  @SuppressWarnings({"unused"})
  protected DrawerFilter() {

    super();
  }

  public DrawerFilter(String title, @DrawableRes int icon,
      @NonNull EventFilterStrategy eventFilterStrategy) {

    super(title, icon);

    this.eventFilterStrategy = eventFilterStrategy;
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
  public boolean isSelectable() {

    return true;
  }

  @Override
  public void select(FragmentActivity activity) {

    activity.setTitle(title);

    Fragment fragment = EventListFragment.newInstance(eventFilterStrategy);

    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
  }

  @Override
  public int getType() {

    return 3;
  }

}
