package com.clloret.days.menu.items;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.clloret.days.R;
import com.clloret.days.events.list.filter.EventFilterByTag;
import com.clloret.days.menu.items.holders.TextViewHolder;
import com.clloret.days.model.entities.Tag;

public class DrawerTag extends DrawerFilter {

  private Tag tag;

  public DrawerTag(@NonNull EventFilterByTag eventFilterByTag,
      @NonNull Tag tag) {

    super(tag.getName(), R.drawable.ic_label_24dp, eventFilterByTag);

    this.tag = tag;
  }

  @Override
  public View getView(LayoutInflater inflater, ViewGroup parent) {

    View convertView = inflater.inflate(R.layout.menu_adapter_texticon, parent, false);

    setView(convertView);

    return convertView;
  }

  @Override
  public int getType() {

    return 3;
  }

  @Override
  public TextViewHolder createViewHolder() {

    return new TextViewHolder();
  }

  public Tag getTag() {

    return tag;
  }

  public void setTag(Tag tag) {

    this.tag = tag;

    updateTitle();
  }

  private void updateTitle() {

    title = tag.getName();
  }
}
