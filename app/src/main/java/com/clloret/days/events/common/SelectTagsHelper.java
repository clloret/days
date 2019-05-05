package com.clloret.days.events.common;

import android.content.res.Resources;
import android.text.TextUtils;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.domain.utils.SelectionMap;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class SelectTagsHelper {

  private final Resources resources;
  private SelectionMap<String, TagViewModel> mapTags = new SelectionMap<>();

  @Inject
  public SelectTagsHelper(Resources resources) {

    this.resources = resources;
  }

  public SelectionMap<String, TagViewModel> getMapTags() {

    return mapTags;
  }

  public void setMapTags(List<TagViewModel> data) {

    Map<String, TagViewModel> stringTagMap = Observable.just(data)
        .concatMap(Observable::fromIterable)
        .toMap(TagViewModel::getId)
        .blockingGet();

    this.mapTags = new SelectionMap<>(stringTagMap);
  }

  public String showSelectedTags() {

    if (mapTags.getSelection().isEmpty()) {
      return resources.getString(R.string.event_details_no_tags);
    }

    List<String> selectedTags = Observable.just(mapTags.getSelection())
        .concatMap(Observable::fromIterable)
        .map(TagViewModel::getName)
        .toList()
        .blockingGet();

    return TextUtils.join(", ", selectedTags);
  }

  public void selectTagsFromEvent(EventViewModel event) {

    for (String tagId : event.getTags()) {
      TagViewModel tag = mapTags.get(tagId);
      if (tag != null) {
        mapTags.addToSelection(tag);
      }
    }
  }

  public void showSelectTagsDialog(BaseMvpActivity activity, SelectTagsHelperListener listener) {

    if (mapTags.size() == 0) {
      listener.onError(resources.getString(R.string.msg_error_no_tags_available));
      return;
    }

    boolean[] checkedTags = new boolean[mapTags.size()];

    int i = 0;
    for (TagViewModel tag : mapTags.values()) {
      if (mapTags.isSelected(tag)) {
        checkedTags[i] = true;
      }
      i++;
    }

    List<String> nameTags = Observable.just(mapTags.values())
        .concatMap(Observable::fromIterable)
        .map(TagViewModel::getName)
        .toList(mapTags.size())
        .blockingGet();

    List<TagViewModel> tags = Observable.just(mapTags.values())
        .concatMap(Observable::fromIterable)
        .toList(mapTags.size())
        .blockingGet();

    SelectTagsDialog dialog = SelectTagsDialog
        .newInstance(resources.getString(R.string.title_select_tags),
            nameTags.toArray(new String[0]), checkedTags,
            new ArrayList<>(tags));
    dialog.show(activity.getSupportFragmentManager(), "tags");
  }

  public void updateSelectedTags(Collection<TagViewModel> selectedItems) {

    mapTags.clearSelection();

    for (TagViewModel tag : selectedItems) {
      mapTags.addToSelection(tag);
    }

    showSelectedTags();
  }

  public void addTagToSelection(TagViewModel tag) {

    mapTags.addToSelection(tag);
  }

  public interface SelectTagsHelperListener {

    void onError(String message);
  }
}
