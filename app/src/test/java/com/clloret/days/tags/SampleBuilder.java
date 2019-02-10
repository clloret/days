package com.clloret.days.tags;

import android.support.annotation.NonNull;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.model.entities.TagViewModel;

public class SampleBuilder {

  public static final String emptyText = "";
  public static final String name = "Mock Event";
  private static final String id = "1";

  @NonNull
  public static Tag createTag() {

    return new Tag(id, name);
  }

  @NonNull
  public static TagViewModel createTagViewModel() {

    return new TagViewModel(id, name);
  }

}
