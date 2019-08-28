package com.clloret.days.dagger.modules;

import static com.clloret.days.data.local.DaysDatabase.MIGRATION_1_2;

import android.content.Context;
import android.text.TextUtils;
import androidx.room.Room;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.mapper.DbEventDataMapper;
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper;
import com.clloret.days.data.local.repository.ReadOnlyEventRepository;
import com.clloret.days.data.local.repository.ReadOnlyTagRepository;
import com.clloret.days.data.local.repository.RoomEventRepository;
import com.clloret.days.data.local.repository.RoomTagRepository;
import com.clloret.days.data.remote.entities.mapper.ApiEventDataMapper;
import com.clloret.days.data.remote.entities.mapper.ApiTagDataMapper;
import com.clloret.days.data.remote.repository.AirtableEventRepository;
import com.clloret.days.data.remote.repository.AirtableTagRepository;
import com.clloret.days.data.repository.AppEventRepository;
import com.clloret.days.data.repository.AppTagRepository;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.repository.TagRepository;
import com.clloret.days.domain.utils.PreferenceUtils;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public abstract class DataModule {

  private static final String DATABASE = "days";

  private static RemoteDataStoreResult checkIsRemoteDataStore(PreferenceUtils preferenceUtils) {

    final boolean useRemoteDataStore = preferenceUtils.getUseRemoteDataStore();
    final String airtableKey = preferenceUtils.getAirtableApiKey();
    final String airtableBase = preferenceUtils.getAirtableBaseId();

    boolean isRemoteDataStore =
        !useRemoteDataStore || TextUtils.isEmpty(airtableKey) || TextUtils.isEmpty(airtableBase);

    return new RemoteDataStoreResult(isRemoteDataStore, airtableKey, airtableBase);
  }

  @Provides
  @Singleton
  static DaysDatabase providesDatabase(Context context) {

    return Room.databaseBuilder(context, DaysDatabase.class, DATABASE)
        .addMigrations(MIGRATION_1_2).build();
  }

  @Provides
  @Singleton
  static AppEventRepository providesAppEventRepository(
      @Named("local") EventRepository localDataStore,
      @Named("remote") EventRepository remoteDataStore) {

    return new AppEventRepository(localDataStore, remoteDataStore);
  }

  @Provides
  @Singleton
  @Named("local")
  static EventRepository providesLocalEventRepository(PreferenceUtils preferenceUtils,
      DaysDatabase db, DbEventDataMapper eventDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils);

    if (remoteDataStoreResult.isRemoteDataStore) {

      return new ReadOnlyEventRepository(db.eventDao(), eventDataMapper);
    } else {

      return new RoomEventRepository(db.eventDao(), eventDataMapper);
    }
  }

  @Provides
  @Singleton
  @Named("remote")
  static EventRepository providesRemoteEventRepository(Context context,
      PreferenceUtils preferenceUtils, DaysDatabase db, DbEventDataMapper dbEventDataMapper,
      ApiEventDataMapper apiEventDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils);

    if (remoteDataStoreResult.isRemoteDataStore) {

      return new RoomEventRepository(db.eventDao(), dbEventDataMapper);
    } else {

      return new AirtableEventRepository(context, remoteDataStoreResult.airtableKey,
          remoteDataStoreResult.airtableBase, apiEventDataMapper);
    }
  }

  @Provides
  @Singleton
  static AppTagRepository providesAppTagRepository(@Named("local") TagRepository localDataStore,
      @Named("remote") TagRepository remoteDataStore) {

    return new AppTagRepository(localDataStore, remoteDataStore);
  }

  @Provides
  @Singleton
  @Named("local")
  static TagRepository providesLocalTagRepository(PreferenceUtils preferenceUtils,
      DaysDatabase db, DbTagDataMapper tagDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils);

    if (remoteDataStoreResult.isRemoteDataStore) {

      return new ReadOnlyTagRepository(db.tagDao(), tagDataMapper);
    } else {

      return new RoomTagRepository(db.tagDao(), tagDataMapper);
    }
  }

  @Provides
  @Singleton
  @Named("remote")
  static TagRepository providesRemoteTagRepository(Context context, PreferenceUtils preferenceUtils,
      DaysDatabase db, DbTagDataMapper dbEventDataMapper, ApiTagDataMapper apiEventDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils);

    if (remoteDataStoreResult.isRemoteDataStore) {

      return new RoomTagRepository(db.tagDao(), dbEventDataMapper);
    } else {

      return new AirtableTagRepository(context, remoteDataStoreResult.airtableKey,
          remoteDataStoreResult.airtableBase, apiEventDataMapper);
    }
  }

  @Binds
  abstract EventRepository bindEventRepository(AppEventRepository impl);

  @Binds
  abstract TagRepository bindTagRepository(AppTagRepository impl);

  @Binds
  abstract CacheSource<Event> bindEventCacheSource(AppEventRepository impl);

  @Binds
  abstract CacheSource<Tag> bindTagCacheSource(AppTagRepository impl);

  private static class RemoteDataStoreResult {

    boolean isRemoteDataStore;
    String airtableKey;
    String airtableBase;

    RemoteDataStoreResult(boolean isRemoteDataStore, String airtableKey, String airtableBase) {

      this.isRemoteDataStore = isRemoteDataStore;
      this.airtableKey = airtableKey;
      this.airtableBase = airtableBase;
    }
  }
}