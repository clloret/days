package com.clloret.days.data.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.VisibleForTesting;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.DbTag;
import com.clloret.days.data.local.entities.converters.DateConverter;
import com.clloret.days.data.local.entities.converters.StringArrayConverter;
import com.clloret.days.data.local.entities.converters.TimeUnitConverter;
import com.clloret.days.data.local.entities.dao.EventDao;
import com.clloret.days.data.local.entities.dao.TagDao;

@Database(entities = {DbEvent.class, DbTag.class}, version = 2)
@TypeConverters({DateConverter.class, StringArrayConverter.class, TimeUnitConverter.class})
public abstract class DaysDatabase extends RoomDatabase {

  public abstract EventDao eventDao();

  public abstract TagDao tagDao();

  @VisibleForTesting
  public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {

      database.execSQL("ALTER TABLE events "
          + " ADD COLUMN `reminder` INTEGER");
      database.execSQL("ALTER TABLE events "
          + " ADD COLUMN `reminder_unit` TEXT");
      database.execSQL("ALTER TABLE events "
          + " ADD COLUMN `time_lapse` INTEGER NOT NULL DEFAULT 0");
      database.execSQL("ALTER TABLE events "
          + " ADD COLUMN `time_lapse_unit` TEXT");
    }
  };
}

