package com.clloret.days.dagger.modules;

import android.content.Context;
import androidx.room.Room;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.mapper.DbEventDataMapper;
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper;
import com.clloret.days.data.local.repository.RoomEventRepository;
import com.clloret.days.data.local.repository.RoomTagRepository;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.repository.TagRepository;
import com.clloret.test_android_common.SampleData;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public abstract class TestDataModule {

  @Provides
  @Singleton
  static DaysDatabase providesDatabase(Context context) {

    DaysDatabase db = Room.inMemoryDatabaseBuilder(context, DaysDatabase.class)
        .allowMainThreadQueries().build();

    SampleData.createEntities(db);

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