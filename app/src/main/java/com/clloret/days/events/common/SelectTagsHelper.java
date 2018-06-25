package com.clloret.days.events.common;

import android.text.TextUtils;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.Tag;
import com.clloret.days.utils.SelectionMap;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SelectTagsHelper {

  private SelectionMap<String, Tag> mapTags = new SelectionMap<>();

  public SelectionMap<String, Tag> getMapTags() {

    return mapTags;
  }

  public void setMapTags(
      List<Tag> data) {

    Map<String, Tag> stringTagMap = Observable.just(data)
        .concatMap(Observable::fromIterable)
        .toMap(Tag::getId)
        .blockingGet();

    this.mapTags = new SelectionMap<>(stringTagMap);
  }

  public String showSelectedTags() {

    List<String> selectedTags = Observable.just(mapTags.getSelection())
        .concatMap(Observable::fromIterable)
        .map(Tag::getName)
        .toList()
        .blockingGet();

    return TextUtils.join(", ", selectedTags);
  }

  public void selectTagsFromEvent(Event event) {

    for (String tagId : event.getTags()) {
      Tag tag = mapTags.get(tagId);
      if (tag != null) {
        mapTags.addToSelection(tag);
      }
    }
  }

  public void showSelectTagsDialog(BaseMvpActivity activity, SelectTagsHelperListener listener) {

    if (mapTags.size() == 0) {
      listener.onError(activity.getString(R.string.msg_error_no_tags_available));
      return;
    }

    boolean[] chekedTags = new boolean[mapTags.size()];

    int i = 0;
    for (Tag tag : mapTags.values()) {
      if (mapTags.isSelected(tag)) {
        chekedTags[i] = true;
      }
      i++;
    }

    List<String> nameTags = Observable.just(mapTags.values())
        .concatMap(Observable::fromIterable)
        .map(Tag::getName)
        .toList(mapTags.size())
        .blockingGet();

    List<Tag> tags = Observable.just(mapTags.values())
        .concatMap(Observable::fromIterable)
        .toList(mapTags.size())
        .blockingGet();

    SelectTagsDialog dialog = SelectTagsDialog
        .newInstance(activity.getString(R.string.title_select_tags),
            nameTags.toArray(new String[nameTags.size()]), chekedTags,
            new ArrayList<>(tags));
    dialog.show(activity.getSupportFragmentManager(), "tags");
  }

  public void updateSelectedTags(Collection<Tag> selectedItems) {

    mapTags.clearSelection();

    for (Tag tag : selectedItems) {
      mapTags.addToSelection(tag);
    }

    showSelectedTags();
  }

  public interface SelectTagsHelperListener {

    void onError(String message);
  }
}
