package com.clloret.days.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.DbTag;
import com.clloret.days.data.local.entities.converters.DateConverter;
import com.clloret.days.data.local.entities.converters.StringArrayConverter;
import com.clloret.days.data.local.entities.converters.TimeUnitConverter;
import com.clloret.days.data.local.entities.dao.EventDao;
import com.clloret.days.data.local.entities.dao.TagDao;

@Database(entities = {DbEvent.class, DbTag.class}, version = 3)
@TypeConverters({DateConverter.class, StringArrayConverter.class, TimeUnitConverter.class})
public abstract class DaysDatabase extends RoomDatabase {

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

  public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
    @Override
    public void migrate(SupportSQLiteDatabase database) {

      database.execSQL(
          "CREATE TABLE IF NOT EXISTS events_new (`id` TEXT NOT NULL, `name` TEXT NOT NULL DEFAULT '', `description` TEXT, `date` INTEGER NOT NULL, `tag_id` TEXT NOT NULL, `favorite` INTEGER NOT NULL, `reminder` INTEGER, `reminder_unit` TEXT NOT NULL DEFAULT 'Day', `time_lapse` INTEGER NOT NULL, `time_lapse_unit` TEXT NOT NULL DEFAULT 'Day', `progress_date` INTEGER, PRIMARY KEY(`id`))");
      database.execSQL(
          "INSERT INTO events_new (id, name, description, date, tag_id, favorite, reminder, reminder_unit, time_lapse, time_lapse_unit)"
              + " SELECT id, name, description, date, tag_id, favorite, reminder, reminder_unit, time_lapse, time_lapse_unit FROM events");
      database.execSQL("DROP TABLE events");
      database.execSQL("ALTER TABLE events_new RENAME TO events");
    }
  };

  public abstract EventDao eventDao();

  public abstract TagDao tagDao();
}

