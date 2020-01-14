package com.clloret.days.data.local.entities.dao;

import androidx.room.Dao;
import androidx.room.Query;
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

  @Query("SELECT * FROM events WHERE tag_id = '' OR tag_id IS NULL")
  Single<List<DbEvent>> loadWithoutAssignedTags();

  @Query("SELECT * FROM events WHERE favorite = 1")
  Single<List<DbEvent>> loadFavorites();

  @Query("SELECT * FROM events WHERE date < :date")
  Single<List<DbEvent>> loadBeforeDate(long date);

  @Query("SELECT * FROM events WHERE date > :date")
  Single<List<DbEvent>> loadAfterDate(long date);

  @Query("SELECT * FROM events WHERE date = :date")
  Single<List<DbEvent>> loadByDate(long date);

  @Query("SELECT * FROM events WHERE reminder IS NOT NULL")
  Single<List<DbEvent>> loadWithReminder();

  @Query("DELETE FROM events")
  void deleteAll();
}