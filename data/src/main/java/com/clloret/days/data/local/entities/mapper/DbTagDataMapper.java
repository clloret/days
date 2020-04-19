package com.clloret.days.data.local.entities.mapper;

import androidx.annotation.NonNull;
import com.clloret.days.data.local.entities.DbTag;
import com.clloret.days.data.remote.entities.mapper.DataMapper;
import com.clloret.days.domain.entities.Tag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbTagDataMapper implements DataMapper<Tag, DbTag> {

  @Inject
  public DbTagDataMapper() {

  }

  @Override
  public Tag toEntity(@NonNull DbTag model) {

    return new Tag(model.getId(), model.getName());
  }

  @Override
  public List<Tag> toEntity(Collection<DbTag> modelCollection) {

    final List<Tag> tagList = new ArrayList<>(20);
    for (DbTag dbTag : modelCollection) {
      final Tag tag = toEntity(dbTag);
      if (tag != null) {
        tagList.add(tag);
      }
    }
    return tagList;
  }

  @Override
  public DbTag fromEntity(@NonNull Tag model, boolean copyId) {

    return new DbTag(model.getId(), model.getName());
  }

  public List<DbTag> fromEntity(Collection<Tag> modelCollection) {

    final List<DbTag> dbTagList = new ArrayList<>(20);
    for (Tag tag : modelCollection) {
      final DbTag dbTag = fromEntity(tag, true);
      if (dbTag != null) {
        dbTagList.add(dbTag);
      }
    }
    return dbTagList;
  }
}
