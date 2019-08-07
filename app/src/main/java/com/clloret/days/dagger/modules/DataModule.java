package com.clloret.days.dagger.modules;

import static com.clloret.days.data.local.DaysDatabase.MIGRATION_1_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import androidx.room.Room;
import com.clloret.days.R;
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
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public abstract class DataModule {

  private static final String DATABASE = "days";

  private static RemoteDataStoreResult checkIsRemoteDataStore(Context context,
      SharedPreferences preferences) {

    boolean remoteDataStore = preferences
        .getBoolean(context.getString(R.string.pref_remote_datastore), false);
    String airtableKey = preferences
        .getString(context.getString(R.string.pref_airtable_api_key), "");
    String airtableBase = preferences
        .getString(context.getString(R.string.pref_airtable_base_id), "");

    boolean isRemoteDataStore =
        !remoteDataStore || TextUtils.isEmpty(airtableKey) || TextUtils.isEmpty(airtableBase);

    return new RemoteDataStoreResult(isRemoteDataStore, airtableKey, airtableBase);
  }

  @Provides
  @Singleton
  static DaysDatabase providesDatabase(Context context) {

    DaysDatabase debug = Room.databaseBuilder(context, DaysDatabase.class, DATABASE)
        .addMigrations(MIGRATION_1_2).build();

    //Timber.d("providesDatabase: %s", debug);
    Log.d("DAYS", String.format("providesDatabase: %s", debug));

    return debug;
//    return Room.databaseBuilder(context, DaysDatabase.class, DATABASE)
//        .addMigrations(MIGRATION_1_2).build();
  }

  @Provides
  @Singleton
  static AppEventRepository providesAppEventRepository(
      @Named("local") EventRepository localDataStore,
      @Named("remote") EventRepository remoteDataStore) {

    AppEventRepository debug = new AppEventRepository(localDataStore, remoteDataStore);

    //Timber.d("providesAppEventRepository: %s", debug);
    Log.d("DAYS", String.format("providesAppEventRepository: %s", debug));

    return debug;
    //return new AppEventRepository(localDataStore, remoteDataStore);
  }

  @Provides
  @Singleton
  @Named("local")
  static EventRepository providesLocalEventRepository(Context context,
      SharedPreferences preferences,
      DaysDatabase db, DbEventDataMapper eventDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(context, preferences);

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
      SharedPreferences preferences,
      DaysDatabase db, DbEventDataMapper dbEventDataMapper, ApiEventDataMapper apiEventDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(context, preferences);

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
  static TagRepository providesLocalTagRepository(Context context, SharedPreferences preferences,
      DaysDatabase db, DbTagDataMapper tagDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(context, preferences);

    if (remoteDataStoreResult.isRemoteDataStore) {

      return new ReadOnlyTagRepository(db.tagDao(), tagDataMapper);
    } else {

      return new RoomTagRepository(db.tagDao(), tagDataMapper);
    }
  }

  @Provides
  @Singleton
  @Named("remote")
  static TagRepository providesRemoteTagRepository(Context context, SharedPreferences preferences,
      DaysDatabase db, DbTagDataMapper dbEventDataMapper, ApiTagDataMapper apiEventDataMapper) {

    RemoteDataStoreResult remoteDataStoreResult = checkIsRemoteDataStore(context, preferences);

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