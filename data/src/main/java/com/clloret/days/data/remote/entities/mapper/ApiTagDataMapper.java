package com.clloret.days.data.remote.entities.mapper;

import com.clloret.days.data.remote.entities.ApiTag;
import com.clloret.days.domain.entities.Tag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApiTagDataMapper {

  public Tag toTag(ApiTag apiTag) {

    Tag tag = null;
    if (apiTag != null) {
      tag = new Tag(apiTag.getId());
      tag.setName(apiTag.getName());
    }
    return tag;
  }

  public List<Tag> toTag(Collection<ApiTag> apiTagCollection) {

    final List<Tag> tagList = new ArrayList<>(20);
    for (ApiTag apiTag : apiTagCollection) {
      final Tag tag = toTag(apiTag);
      if (tag != null) {
        tagList.add(tag);
      }
    }
    return tagList;
  }

  public ApiTag fromTag(Tag tag, boolean copyId) {

    ApiTag dbTag = null;
    if (tag != null) {
      dbTag = new ApiTag(copyId ? tag.getId() : null);
      dbTag.setName(tag.getName());
    }
    return dbTag;
  }

  public List<ApiTag> fromTag(Collection<Tag> tagCollection) {

    final List<ApiTag> apiTagList = new ArrayList<>(20);
    for (Tag tag : tagCollection) {
      final ApiTag apiTag = fromTag(tag, true);
      if (apiTag != null) {
        apiTagList.add(apiTag);
      }
    }
    return apiTagList;
  }
}
