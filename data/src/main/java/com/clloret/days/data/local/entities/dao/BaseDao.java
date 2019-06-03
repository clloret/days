package com.clloret.days.data.local.entities.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import java.util.List;

public interface BaseDao<T> {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<T> entity);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(T entity);

  @Update
  void update(T entity);

  @Delete
  int delete(T entity);
}
