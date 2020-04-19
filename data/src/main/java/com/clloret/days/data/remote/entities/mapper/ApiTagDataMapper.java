package com.clloret.days.data.remote.entities.mapper;

import androidx.annotation.NonNull;
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
  public Tag toEntity(@NonNull ApiTag model) {

    return new Tag(model.getId(), model.getName());
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
  public ApiTag fromEntity(@NonNull Tag model, boolean copyId) {

    return new ApiTag(copyId ? model.getId() : null, model.getName());
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
