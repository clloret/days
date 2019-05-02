package com.clloret.days.data.remote.entities.mapper;

import com.clloret.days.data.remote.entities.ApiTag;
import com.clloret.days.domain.entities.Tag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApiTagDataMapper implements DataMapper<Tag, ApiTag> {

  @Inject
  public ApiTagDataMapper() {

  }

  @Override
  public Tag toEntity(ApiTag model) {

    Tag tag = null;
    if (model != null) {
      tag = new Tag(model.getId());
      tag.setName(model.getName());
    }
    return tag;
  }

  @Override
  public List<Tag> toEntity(Collection<ApiTag> modelCollection) {

    final List<Tag> tagList = new ArrayList<>(20);
    for (ApiTag apiTag : modelCollection) {
      final Tag tag = toEntity(apiTag);
      if (tag != null) {
        tagList.add(tag);
      }
    }
    return tagList;
  }

  @Override
  public ApiTag fromEntity(Tag model, boolean copyId) {

    ApiTag dbTag = null;
    if (model != null) {
      dbTag = new ApiTag(copyId ? model.getId() : null);
      dbTag.setName(model.getName());
    }
    return dbTag;
  }

  @Override
  public List<ApiTag> fromEntity(Collection<Tag> modelCollection) {

    final List<ApiTag> apiTagList = new ArrayList<>(20);
    for (Tag tag : modelCollection) {
      final ApiTag apiTag = fromEntity(tag, true);
      if (apiTag != null) {
        apiTagList.add(apiTag);
      }
    }
    return apiTagList;
  }
}
