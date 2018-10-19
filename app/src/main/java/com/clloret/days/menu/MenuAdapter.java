package com.clloret.days.menu;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.clloret.days.R;
import com.clloret.days.activities.AboutActivity;
import com.clloret.days.activities.SettingsActivity;
import com.clloret.days.events.list.filter.EventFilterAll;
import com.clloret.days.events.list.filter.EventFilterByFavorite;
import com.clloret.days.events.list.filter.EventFilterByTag;
import com.clloret.days.menu.items.DrawerAction;
import com.clloret.days.menu.items.DrawerFilter;
import com.clloret.days.menu.items.DrawerMenuItem;
import com.clloret.days.menu.items.DrawerSeparator;
import com.clloret.days.menu.items.DrawerSubheader;
import com.clloret.days.menu.items.DrawerTag;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.tags.create.TagCreateActivity;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAdapter extends BaseAdapter {

  private static final int TYPE_COUNT = 4;

  private final List<Collection<DrawerMenuItem>> sections = new ArrayList<>();
  private final Map<String, DrawerTag> tags = new HashMap<>();
  private final CompositeDisposable disposable = new CompositeDisposable();
  private Context context;

  public MenuAdapter(@NonNull Context context) {

    this.context = context;
  }

  public void dispose() {

    disposable.dispose();
  }

  public void setData(List<TagViewModel> data) {

    showTags(data);
  }

  private void showTags(List<TagViewModel> data) {

    tags.clear();

    Disposable subscribe = Observable.fromIterable(data)
        .map(
            tag -> new DrawerTag(new EventFilterByTag(tag.getId()), tag))
        .subscribe(drawerTag -> tags.put(drawerTag.getTag().getId(), drawerTag));
    disposable.add(subscribe);

    Collection menuItems = tags.values();
    if (!sections.contains(menuItems)) {
      //noinspection unchecked
      sections.add(1, menuItems);
    }

    notifyDataSetChanged();
  }

  private List<DrawerMenuItem> createSection1() {

    List<DrawerMenuItem> list = new ArrayList<>(4);

    list.add(new DrawerFilter(context.getString(R.string.action_all), R.drawable.ic_inbox_24dp,
        new EventFilterAll()));
    list.add(new DrawerFilter(context.getString(R.string.action_favorites),
        R.drawable.ic_favorite_24dp, new EventFilterByFavorite()));

    list.add(new DrawerSeparator());
    list.add(new DrawerSubheader(context.getString(R.string.event_details_tags)));

    return list;
  }

  private List<DrawerMenuItem> createSection2() {

    List<DrawerMenuItem> list = new ArrayList<>(5);

    list
        .add(new DrawerFilter(context.getString(R.string.action_no_tag),
            R.drawable.ic_label_outline_24dp, new EventFilterByTag("")));
    list.add(new DrawerAction(
        context.getString(R.string.action_new_tag),
        R.drawable.ic_add_24dp,
        new Intent(context, TagCreateActivity.class),
        0x00));

    list.add(new DrawerSeparator());

    list.add(new DrawerAction(
        context.getString(R.string.action_settings),
        R.drawable.ic_settings_24dp,
        new Intent(context, SettingsActivity.class),
        0x00));

    list.add(new DrawerAction(
        context.getString(R.string.action_about),
        R.drawable.ic_info_24dp,
        new Intent(context, AboutActivity.class),
        0x00));

    return list;
  }

  public void populateList() {

    sections.clear();

    sections.add(createSection1());
    sections.add(createSection2());
  }

  @Override
  public int getCount() {

    final List<DrawerMenuItem> items = getConcatList();

    return items.size();
  }

  private List<DrawerMenuItem> getConcatList() {

    return Observable.fromIterable(sections)
        .concatMapIterable(lists -> lists)
        .toList()
        .blockingGet();
  }

  @Override
  public DrawerMenuItem getItem(int i) {

    final List<DrawerMenuItem> items = getConcatList();

    return items.get(i);
  }

  @Override
  public long getItemId(int i) {

    return i;
  }

  @NonNull
  @Override
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {

    DrawerMenuItem item = getItem(position);

    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = item.getView(inflater, parent);
    } else {

      item.setView(convertView);
    }

    item.populate();

    return convertView;
  }

  public void addTag(TagViewModel tag) {

    DrawerTag drawerTag = new DrawerTag(new EventFilterByTag(tag.getId()), tag);
    tags.put(tag.getId(), drawerTag);

    notifyDataSetChanged();
  }

  public void updateTag(TagViewModel tag) {

    if (tags.containsKey(tag.getId())) {
      DrawerTag drawerTag = tags.get(tag.getId());
      drawerTag.setTag(tag);

      notifyDataSetChanged();
    }
  }

  public void deleteTag(TagViewModel tag) {

    tags.remove(tag.getId());
    notifyDataSetChanged();
  }

  @Override
  public boolean isEnabled(int position) {

    DrawerMenuItem menuItem = getItem(position);

    return menuItem.isClickable();
  }

  @Override
  public int getItemViewType(int position) {

    DrawerMenuItem menuItem = getItem(position);

    return menuItem.getType();
  }

  @Override
  public int getViewTypeCount() {

    return TYPE_COUNT;
  }
}
