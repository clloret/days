package com.clloret.days.data;

import static com.clloret.days.data.local.DaysDatabase.MIGRATION_1_2;
import static com.clloret.days.data.local.DaysDatabase.MIGRATION_2_3;
import static com.google.common.truth.Truth.assertThat;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.DbTag;
import com.clloret.days.domain.entities.Event.TimeUnit;
import java.io.IOException;
import java.util.Date;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MigrationTest {

  private static final String TEST_DB_NAME = "migration-test";
  private static final DbEvent TEST_DB_EVENT = new DbEvent(
      "id",
      "name",
      "description",
      new Date(),
      false,
      6,
      TimeUnit.MONTH,
      1,
      TimeUnit.YEAR
  );
  private static final DbTag TEST_DB_TAG = new DbTag(
      "id",
      "name"
  );

  @SuppressWarnings("ConstantConditions")
  @Rule
  public MigrationTestHelper migrationTestHelper =
      new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
          DaysDatabase.class.getCanonicalName(),
          new FrameworkSQLiteOpenHelperFactory());

  @Test
  public void migrationFrom1To2_Always_ContainsCorrectData() throws IOException {

    SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 1);

    insertEvent(TEST_DB_EVENT, db, 1);

    db.close();

    migrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true,
        MIGRATION_1_2);
    migrationTestHelper.closeWhenFinished(db);

    // I can only check the latest version of the database
  }

  @Test
  public void migrationFrom2To3_Always_ContainsCorrectData() throws IOException {

    SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 2);

    insertEvent(TEST_DB_EVENT, db, 3);
    insertTag(TEST_DB_TAG, db, 3);

    db.close();

    migrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 3, true,
        MIGRATION_2_3);
    migrationTestHelper.closeWhenFinished(db);

    DbEvent dbEvent = getMigratedRoomDatabase().eventDao().getEvent();

    assertThat(dbEvent.getId()).isEqualTo(TEST_DB_EVENT.getId());
    assertThat(dbEvent.getName()).isEqualTo(TEST_DB_EVENT.getName());
    assertThat(dbEvent.getReminder()).isEqualTo(TEST_DB_EVENT.getReminder());
    assertThat(dbEvent.getReminderUnit()).isEqualTo(TEST_DB_EVENT.getReminderUnit());
    assertThat(dbEvent.getTimeLapse()).isEqualTo(TEST_DB_EVENT.getTimeLapse());
    assertThat(dbEvent.getTimeLapseUnit()).isEqualTo(TEST_DB_EVENT.getTimeLapseUnit());

    DbTag dbTag = getMigratedRoomDatabase().tagDao().getTagById("id");

    assertThat(dbTag.getId()).isEqualTo(TEST_DB_TAG.getId());
    assertThat(dbTag.getName()).isEqualTo(TEST_DB_TAG.getName());
  }

  @Test
  public void startInVersion2_Always_ContainsCorrectData() throws IOException {

    SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 2);
    insertEvent(TEST_DB_EVENT, db, 2);
    insertTag(TEST_DB_TAG, db, 2);
    db.close();

    DaysDatabase daysDatabase = getMigratedRoomDatabase();
    DbEvent dbEvent = daysDatabase.eventDao().getEvent();

    assertThat(dbEvent.getId()).isEqualTo(TEST_DB_EVENT.getId());
    assertThat(dbEvent.getName()).isEqualTo(TEST_DB_EVENT.getName());
    assertThat(dbEvent.getReminder()).isEqualTo(TEST_DB_EVENT.getReminder());
    assertThat(dbEvent.getReminderUnit()).isEqualTo(TEST_DB_EVENT.getReminderUnit());
    assertThat(dbEvent.getTimeLapse()).isEqualTo(TEST_DB_EVENT.getTimeLapse());
    assertThat(dbEvent.getTimeLapseUnit()).isEqualTo(TEST_DB_EVENT.getTimeLapseUnit());

    DbTag dbTag = getMigratedRoomDatabase().tagDao().getTagById("id");

    assertThat(dbTag.getId()).isEqualTo(TEST_DB_TAG.getId());
    assertThat(dbTag.getName()).isEqualTo(TEST_DB_TAG.getName());
  }

  private DaysDatabase getMigratedRoomDatabase() {

    DaysDatabase database = Room
        .databaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
            DaysDatabase.class, TEST_DB_NAME)
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .build();
    migrationTestHelper.closeWhenFinished(database);
    return database;
  }

  @SuppressWarnings("SameParameterValue")
  private void insertEvent(DbEvent dbEvent, SupportSQLiteDatabase db, int version) {

    ContentValues values = new ContentValues();
    values.put("id", dbEvent.getId());
    values.put("name", dbEvent.getName());
    values.put("date", dbEvent.getDate().getTime());
    values.put("favorite", dbEvent.getFavorite());
    values.put("tag_id", "");

    if (version >= 2) {
      values.put("reminder", dbEvent.getReminder());
      values.put("time_lapse", dbEvent.getTimeLapse());
      values.put("reminder_unit", dbEvent.getReminderUnit().toString());
      values.put("time_lapse_unit", dbEvent.getTimeLapseUnit().toString());
    }

    db.insert("events", SQLiteDatabase.CONFLICT_REPLACE, values);
  }

  @SuppressWarnings("SameParameterValue")
  private void insertTag(DbTag dbTag, SupportSQLiteDatabase db, int version) {

    ContentValues values = new ContentValues();
    values.put("id", dbTag.getId());
    values.put("name", dbTag.getName());

    db.insert("tags", SQLiteDatabase.CONFLICT_REPLACE, values);
  }

}
