package com.clloret.days.tags;

import android.support.annotation.NonNull;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.model.entities.TagViewModel;

public class SampleBuilder {

  public static final String EMPTY_TEXT = "";
  public static final String NAME = "Mock Event";
  private static final String ID = "1";

  @NonNull
  public static Tag createTag() {

    return new Tag(ID, NAME);
  }

  @NonNull
  public static TagViewModel createTagViewModel() {

    return new TagViewModel(ID, NAME);
  }

}
