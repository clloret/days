package com.clloret.days.data.local.entities.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;
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
