package com.clloret.days.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.clloret.days.data.local.entities.DbTag;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface TagDao {

  @Query("SELECT * FROM tags")
  Single<List<DbTag>> getAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<DbTag> tags);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(DbTag tag);

  @Update
  void update(DbTag tag);

  @Delete
  int delete(DbTag tag);

  @Query("DELETE FROM tags")
  void deleteAll();

}
