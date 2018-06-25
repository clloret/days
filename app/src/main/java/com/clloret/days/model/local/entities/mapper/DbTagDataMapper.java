package com.clloret.days.model.local.entities.mapper;

import com.clloret.days.model.entities.Tag;
import com.clloret.days.model.local.entities.DbTag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DbTagDataMapper {

  private Tag toTag(DbTag dbTag) {

    Tag tag = null;
    if (dbTag != null) {
      tag = new Tag(dbTag.getId());
      tag.setName(dbTag.getName());
    }
    return tag;
  }

  public List<Tag> toTag(Collection<DbTag> dbTagCollection) {

    final List<Tag> tagList = new ArrayList<>(20);
    for (DbTag dbTag : dbTagCollection) {
      final Tag tag = toTag(dbTag);
      if (tag != null) {
        tagList.add(tag);
      }
    }
    return tagList;
  }

  public DbTag fromTag(Tag tag) {

    DbTag dbTag = null;
    if (tag != null) {
      dbTag = new DbTag(tag.getId());
      dbTag.setName(tag.getName());
    }
    return dbTag;
  }

  public List<DbTag> fromTag(Collection<Tag> tagCollection) {

    final List<DbTag> dbTagList = new ArrayList<>(20);
    for (Tag tag : tagCollection) {
      final DbTag dbTag = fromTag(tag);
      if (dbTag != null) {
        dbTagList.add(dbTag);
      }
    }
    return dbTagList;
  }
}
