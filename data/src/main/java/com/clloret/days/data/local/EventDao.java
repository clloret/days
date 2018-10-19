package com.clloret.days.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.clloret.days.data.local.entities.DbEvent;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface EventDao {

  @Query("SELECT * FROM events")
  Single<List<DbEvent>> getAll();

  @Query("SELECT * FROM events WHERE tag_id LIKE (:tagId)")
  Single<List<DbEvent>> loadByTagsIds(String tagId);

  //  @Query("SELECT * FROM events WHERE tag_id IS NULL")
  @Query("SELECT * FROM events WHERE tag_id = ''")
  Single<List<DbEvent>> loadWithoutAssignedTags();

  @Query("SELECT * FROM events WHERE favorite = 1")
  Single<List<DbEvent>> loadFavorites();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<DbEvent> events);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(DbEvent event);

  @Update
  void update(DbEvent event);

  @Delete
  int delete(DbEvent event);

  @Query("DELETE FROM events")
  void deleteAll();
}