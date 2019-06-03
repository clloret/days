package com.clloret.days.dagger.modules;

import android.content.Context;
import androidx.room.Room;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.mapper.DbEventDataMapper;
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper;
import com.clloret.days.data.local.repository.RoomEventRepository;
import com.clloret.days.data.local.repository.RoomTagRepository;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.repository.TagRepository;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.Date;
import javax.inject.Singleton;

@Module
public abstract class TestDataModule {

  public static final String TEST_EVENT_1_ID = "fba54d4f-82d9-459c-ae67-ca3253a7ed3e";
  public static final String TEST_EVENT_1_NAME = "Test event 1";
  public static final String TEST_EVENT_2_ID = "50b8a4e9-3349-419d-9e8b-5a1f3e365734";
  public static final String TEST_EVENT_2_NAME = "Test event 2";
  public static final String TEST_EVENT_3_ID = "fdfbc340-5781-4b71-9aa7-b5815a362b41";
  public static final String TEST_EVENT_3_NAME = "Test event 3";

  private static void createTestDbEvents(DaysDatabase db) {

    Date now = new Date();
    db.eventDao()
        .insert(createDbEvent(TEST_EVENT_1_ID, TEST_EVENT_1_NAME, now));
    db.eventDao()
        .insert(createDbEvent(TEST_EVENT_2_ID, TEST_EVENT_2_NAME, now));
    db.eventDao()
        .insert(createDbEvent(TEST_EVENT_3_ID, TEST_EVENT_3_NAME, now));
  }

  private static DbEvent createDbEvent(String id, String name, Date date) {

    DbEvent dbEvent = new DbEvent(id);
    dbEvent.setDate(date);
    dbEvent.setName(name);

    return dbEvent;
  }

  @Provides
  @Singleton
  static DaysDatabase providesDatabase(Context context) {

    DaysDatabase db = Room.inMemoryDatabaseBuilder(context, DaysDatabase.class)
        .allowMainThreadQueries().build();

    createTestDbEvents(db);

    return db;
  }

  @Provides
  @Singleton
  static RoomEventRepository providesLocalEventRepository(DaysDatabase db,
      DbEventDataMapper eventDataMapper) {

    return new RoomEventRepository(db.eventDao(), eventDataMapper);
  }

  @Provides
  @Singleton
  static RoomTagRepository providesLocalTagRepository(DaysDatabase db,
      DbTagDataMapper tagDataMapper) {

    return new RoomTagRepository(db.tagDao(), tagDataMapper);
  }

  @Binds
  abstract EventRepository bindEventRepository(RoomEventRepository impl);

  @Binds
  abstract TagRepository bindTagRepository(RoomTagRepository impl);

  @Binds
  abstract CacheSource<Event> bindEventCacheSource(RoomEventRepository impl);

  @Binds
  abstract CacheSource<Tag> bindTagCacheSource(RoomTagRepository impl);

}