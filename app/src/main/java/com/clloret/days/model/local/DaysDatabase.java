package com.clloret.days.model.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.clloret.days.model.local.entities.DbEvent;
import com.clloret.days.model.local.entities.DbTag;

@Database(entities = {DbEvent.class, DbTag.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, StringArrayConverter.class})
public abstract class DaysDatabase extends RoomDatabase {

  public abstract EventDao eventDao();

  public abstract TagDao tagDao();
}
