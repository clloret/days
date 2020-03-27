package com.clloret.days.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.annotation.NonNull;
import com.clloret.days.R;
import com.clloret.days.activities.AboutActivity;
import com.clloret.days.domain.events.filter.EventFilterAll;
import com.clloret.days.domain.events.filter.EventFilterByFavorite;
import com.clloret.days.domain.events.filter.EventFilterByFuture;
import com.clloret.days.domain.events.filter.EventFilterByPast;
import com.clloret.days.domain.events.filter.EventFilterByReminder;
import com.clloret.days.domain.events.filter.EventFilterByTag;
import com.clloret.days.domain.events.filter.EventFilterByToday;
import com.clloret.days.domain.tags.order.TagSortFactory;
import com.clloret.days.domain.tags.order.TagSortFactory.SortType;
import com.clloret.days.domain.tags.order.TagSortable;
import com.clloret.days.domain.utils.SortedValueMap;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.menu.items.DrawerAction;
import com.clloret.days.menu.items.DrawerFilter;
import com.clloret.days.menu.items.DrawerMenuItem;
import com.clloret.days.menu.items.DrawerSeparator;
import com.clloret.days.menu.items.DrawerSubheader;
import com.clloret.days.menu.items.DrawerTag;
import com.clloret.days.menu.items.DrawerTagSelectedMgr;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.settings.SettingsActivity;
import com.clloret.days.tags.create.TagCreateActivity;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.joda.time.LocalDate;

public class MenuAdapter extends BaseAdapter {

  private static final int TYPE_COUNT = 4;
  private static final int SECTION1_INDEX = 1;
  private static final int SECTION_TAGS_INDEX = 2;
  private static final int SECTION2_INDEX = 3;
  private static final int SECTION1_INITIAL_CAPACITY = 8;
  private static final int SECTION2_INITIAL_CAPACITY = 6;
  private static final String CHANGELOG_URL = "https://github.com/clloret/days/blob/master/CHANGELOG.md";

  private final Map<Integer, Collection<DrawerMenuItem>> sections = new HashMap<>();
  private final SortedValueMap<String, DrawerTag, TagSortable> tags = new SortedValueMap<>(
      TagSortFactory.makeTagSort(SortType.NAME));
  private final CompositeDisposable disposable = new CompositeDisposable();
  private final Context context;
  private final DrawerTagSelectedMgr drawerTagSelectedMgr;
  private final TimeProvider timeProvider;

  public MenuAdapter(@NonNull Context context, @NonNull DrawerTagSelectedMgr drawerTagSelectedMgr,
      @NonNull TimeProvider timeProvider) {

    super();

    this.context = context;
    this.drawerTagSelectedMgr = drawerTagSelectedMgr;
    this.timeProvider = timeProvider;
  }

  @Override
  public int getCount() {

    final List<DrawerMenuItem> items = getConcatList();

    return items.size();
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
  public View getView(final int position, final View convertView, final @NonNull ViewGroup parent) {

    DrawerMenuItem item = getItem(position);

    View resultView = convertView;
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(context);
      resultView = item.getView(inflater, parent);
    } else {

      item.setView(convertView);
    }

    item.populate();

    return resultView;
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

  void dispose() {

    disposable.dispose();
  }

  public void setData(List<TagViewModel> data) {

    showTags(data);
  }

  private void showTags(List<TagViewModel> data) {

    tags.clear();

    Disposable subscribe = Observable.fromIterable(data)
        .map(
            tag -> new DrawerTag(new EventFilterByTag(tag.getId()), tag, drawerTagSelectedMgr))
        .subscribe(drawerTag -> tags.put(drawerTag.getTag().getId(), drawerTag));
    disposable.add(subscribe);

    Collection tagCollection = tags.sortedValues();
    //noinspection unchecked
    sections.put(SECTION_TAGS_INDEX, tagCollection);

    notifyDataSetChanged();
  }

  private List<DrawerMenuItem> createSection1() {

    List<DrawerMenuItem> list = new ArrayList<>(SECTION1_INITIAL_CAPACITY);

    list.add(
        new DrawerFilter(context.getString(R.string.action_filter_all), R.drawable.ic_inbox_24dp,
            new EventFilterAll()));
    list.add(new DrawerFilter(context.getString(R.string.action_filter_favorites),
        R.drawable.ic_favorite_24dp, new EventFilterByFavorite()));

    LocalDate currentDate = timeProvider.getCurrentDate();

    list.add(new DrawerFilter(context.getString(R.string.action_filter_today),
        R.drawable.ic_today_24dp, new EventFilterByToday(currentDate)));
    list.add(new DrawerFilter(context.getString(R.string.action_filter_upcoming),
        R.drawable.ic_upcoming_24dp, new EventFilterByFuture(currentDate)));
    list.add(new DrawerFilter(context.getString(R.string.action_filter_previous),
        R.drawable.ic_previous_24dp, new EventFilterByPast(currentDate)));
    list.add(new DrawerFilter(context.getString(R.string.action_filter_with_reminder),
        R.drawable.ic_notifications_24dp, new EventFilterByReminder()));

    list.add(new DrawerSeparator());
    list.add(new DrawerSubheader(context.getString(R.string.event_details_tags)));

    return list;
  }

  private List<DrawerMenuItem> createSection2() {

    List<DrawerMenuItem> list = new ArrayList<>(SECTION2_INITIAL_CAPACITY);

    list
        .add(new DrawerFilter(context.getString(R.string.action_filter_no_tag),
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

    list.add(createOpenChangelogDrawerAction());

    list.add(new DrawerAction(
        context.getString(R.string.action_about),
        R.drawable.ic_info_24dp,
        new Intent(context, AboutActivity.class),
        0x00));

    return list;
  }

  private DrawerAction createOpenChangelogDrawerAction() {

    final Uri uri = Uri.parse(CHANGELOG_URL);
    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);

    return new DrawerAction(
        context.getString(R.string.action_changelog),
        R.drawable.ic_changelog_24dp,
        intent,
        0x00);
  }

  void populateList() {

    sections.clear();

    sections.put(SECTION1_INDEX, createSection1());
    sections.put(SECTION2_INDEX, createSection2());
  }

  private List<DrawerMenuItem> getConcatList() {

    return Observable.just(sections)
        .concatMapIterable(Map::entrySet)
        .sorted((entry1, entry2) -> Integer
            .compare(entry1.getKey(), entry2.getKey()))
        .map(Entry::getValue)
        .flatMapIterable(drawerMenuItems -> drawerMenuItems)
        .toList()
        .blockingGet();
  }

  void addTag(TagViewModel tag) {

    DrawerTag drawerTag = new DrawerTag(new EventFilterByTag(tag.getId()), tag,
        drawerTagSelectedMgr);
    tags.put(tag.getId(), drawerTag);

    notifyDataSetChanged();
  }

  void updateTag(TagViewModel tag) {

    if (tags.containsKey(tag.getId())) {
      DrawerTag drawerTag = Objects.requireNonNull(tags.get(tag.getId()));
      drawerTag.setTag(tag);

      tags.refreshValues();

      notifyDataSetChanged();
    }
  }

  void deleteTag(TagViewModel tag) {

    tags.remove(tag.getId());
    notifyDataSetChanged();
  }

}
