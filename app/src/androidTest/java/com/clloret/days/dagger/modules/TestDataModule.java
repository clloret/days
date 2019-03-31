package com.clloret.days.dagger.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.LocalDataStore;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import dagger.Module;
import dagger.Provides;
import java.util.Date;
import javax.inject.Singleton;

@Module
public class TestDataModule {

  public static final String TEST_EVENT_1_ID = "fba54d4f-82d9-459c-ae67-ca3253a7ed3e";
  public static final String TEST_EVENT_1_NAME = "Test event 1";
  public static final String TEST_EVENT_2_ID = "50b8a4e9-3349-419d-9e8b-5a1f3e365734";
  public static final String TEST_EVENT_2_NAME = "Test event 2";

  private static void createTestDbEvents(DaysDatabase db) {

    Date now = new Date();
    db.eventDao()
        .insert(createDbEvent(TEST_EVENT_1_ID, TEST_EVENT_1_NAME, now));
    db.eventDao()
        .insert(createDbEvent(TEST_EVENT_2_ID, TEST_EVENT_2_NAME, now));
  }

  private static DbEvent createDbEvent(String id, String name, Date date) {

    DbEvent dbEvent = new DbEvent(id);
    dbEvent.setDate(date);
    dbEvent.setName(name);

    return dbEvent;
  }

  @Provides
  @Singleton
  AppDataStore providesAppDataStore(Context context) {

    DaysDatabase db = Room.inMemoryDatabaseBuilder(context, DaysDatabase.class)
        .allowMainThreadQueries().build();

    createTestDbEvents(db);

    return new LocalDataStore(db);
  }

  @Provides
  @Singleton
  EventViewModelMapper providesEventViewModelMapper() {

    return new EventViewModelMapper();
  }

  @Provides
  @Singleton
  TagViewModelMapper providesTagViewModelMapper() {

    return new TagViewModelMapper();
  }

}