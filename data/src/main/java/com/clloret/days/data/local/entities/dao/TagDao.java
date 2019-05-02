package com.clloret.days.data.local.entities.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.clloret.days.data.local.entities.DbTag;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface TagDao extends BaseDao<DbTag> {

  @Query("SELECT * FROM tags")
  Single<List<DbTag>> getAll();

  @Query("SELECT * FROM tags WHERE id = :tagId")
  DbTag getTagById(String tagId);

  @Query("DELETE FROM tags")
  void deleteAll();

}
