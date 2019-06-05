package com.clloret.days.data;

import static com.clloret.days.data.local.DaysDatabase.MIGRATION_1_2;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.platform.app.InstrumentationRegistry;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.converters.DateConverter;
import com.clloret.days.domain.entities.Event.TimeUnit;
import java.io.IOException;
import java.util.Date;
import org.junit.Rule;
import org.junit.Test;

public class MigrationTest {

  private static final String TEST_DB_NAME = "migration-test";
  private static final DbEvent EVENT = new DbEvent("id", "name", "description",
      new Date(), false, 6, TimeUnit.MONTH, 1, TimeUnit.YEAR);

  @Rule
  public MigrationTestHelper migrationTestHelper =
      new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
          DaysDatabase.class.getCanonicalName(),
          new FrameworkSQLiteOpenHelperFactory());

  @Test
  public void migrationFrom1To2_Always_ContainsCorrectData() throws IOException {

    SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 1);

    insertEvent(EVENT.getId(), EVENT.getName(), db);

    db.close();

    migrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true,
        MIGRATION_1_2);

    DbEvent dbEvent = getMigratedRoomDatabase().eventDao().getEvent();

    assertThat(dbEvent.getId(), equalTo(EVENT.getId()));
    assertThat(dbEvent.getName(), equalTo(EVENT.getName()));
    assertThat(dbEvent.getReminder(), nullValue());
    assertThat(dbEvent.getReminderUnit(), nullValue());
    assertThat(dbEvent.getTimeLapse(), equalTo(0));
    assertThat(dbEvent.getTimeLapseUnit(), nullValue());
  }

  @Test
  public void startInVersion2_Always_ContainsCorrectData() throws IOException {

    SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 2);
    insertEvent(EVENT, db);
    db.close();

    DaysDatabase daysDatabase = getMigratedRoomDatabase();
    DbEvent dbEvent = daysDatabase.eventDao().getEvent();

    assertThat(dbEvent.getId(), equalTo(EVENT.getId()));
    assertThat(dbEvent.getName(), equalTo(EVENT.getName()));
    assertThat(dbEvent.getDate(), equalTo(EVENT.getDate()));
    assertThat(dbEvent.getReminder(), equalTo(EVENT.getReminder()));
    assertThat(dbEvent.getReminderUnit(), equalTo(EVENT.getReminderUnit()));
    assertThat(dbEvent.getTimeLapse(), equalTo(EVENT.getTimeLapse()));
    assertThat(dbEvent.getTimeLapseUnit(), equalTo(EVENT.getTimeLapseUnit()));
  }

  private DaysDatabase getMigratedRoomDatabase() {

    DaysDatabase database = Room
        .databaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
            DaysDatabase.class, TEST_DB_NAME)
        .addMigrations(MIGRATION_1_2)
        .build();
    migrationTestHelper.closeWhenFinished(database);
    return database;
  }

  private void insertEvent(String id, String name, SupportSQLiteDatabase db) {

    ContentValues values = new ContentValues();
    values.put("id", id);
    values.put("name", name);
    values.put("favorite", false);

    db.insert("events", SQLiteDatabase.CONFLICT_REPLACE, values);
  }

  private void insertEvent(DbEvent dbEvent, SupportSQLiteDatabase db) {

    ContentValues values = new ContentValues();
    values.put("id", dbEvent.getId());
    values.put("name", dbEvent.getName());
    values.put("date", DateConverter.toTimestamp(dbEvent.getDate()));
    values.put("favorite", dbEvent.isFavorite());
    values.put("reminder", dbEvent.getReminder());
    values.put("reminder_unit", dbEvent.getReminderUnit().toString());
    values.put("time_lapse", dbEvent.getTimeLapse());
    values.put("time_lapse_unit", dbEvent.getTimeLapseUnit().toString());

    db.insert("events", SQLiteDatabase.CONFLICT_REPLACE, values);
  }
}
