package com.clloret.days.data.local.entities.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.clloret.days.data.local.entities.DbEvent;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface EventDao extends BaseDao<DbEvent> {

  @Query("SELECT * FROM events")
  Single<List<DbEvent>> getAll();

  @Query("SELECT * FROM events LIMIT 1")
  DbEvent getEvent();

  @Query("SELECT * FROM events WHERE id = :eventId")
  DbEvent getEventById(String eventId);

  @Query("SELECT * FROM events WHERE tag_id LIKE (:tagId)")
  Single<List<DbEvent>> loadByTagsIds(String tagId);

  //  @Query("SELECT * FROM events WHERE tag_id IS NULL")
  @Query("SELECT * FROM events WHERE tag_id = ''")
  Single<List<DbEvent>> loadWithoutAssignedTags();

  @Query("SELECT * FROM events WHERE favorite = 1")
  Single<List<DbEvent>> loadFavorites();

  @Query("DELETE FROM events")
  void deleteAll();
}