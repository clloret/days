package com.clloret.days.dagger.modules

import android.content.Context
import android.text.TextUtils
import androidx.room.Room
import com.clloret.days.data.cache.CacheSource
import com.clloret.days.data.local.DaysDatabase
import com.clloret.days.data.local.entities.mapper.DbEventDataMapper
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper
import com.clloret.days.data.local.repository.ReadOnlyEventRepository
import com.clloret.days.data.local.repository.ReadOnlyTagRepository
import com.clloret.days.data.local.repository.RoomEventRepository
import com.clloret.days.data.local.repository.RoomTagRepository
import com.clloret.days.data.remote.entities.mapper.ApiEventDataMapper
import com.clloret.days.data.remote.entities.mapper.ApiTagDataMapper
import com.clloret.days.data.remote.repository.AirtableEventRepository
import com.clloret.days.data.remote.repository.AirtableTagRepository
import com.clloret.days.data.repository.AppEventRepository
import com.clloret.days.data.repository.AppTagRepository
import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.entities.Tag
import com.clloret.days.domain.repository.EventRepository
import com.clloret.days.domain.repository.TagRepository
import com.clloret.days.domain.utils.PreferenceUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class DataModule {

  @Binds
  abstract fun bindEventRepository(impl: AppEventRepository): EventRepository

  @Binds
  abstract fun bindTagRepository(impl: AppTagRepository): TagRepository

  @Binds
  abstract fun bindEventCacheSource(impl: AppEventRepository): CacheSource<Event>

  @Binds
  abstract fun bindTagCacheSource(impl: AppTagRepository): CacheSource<Tag>

  class RemoteDataStoreResult internal constructor(
          var isRemoteDataStore: Boolean,
          var airtableKey: String,
          var airtableBase: String)

  @Module
  companion object {
    private const val DATABASE = "days"

    private fun checkIsRemoteDataStore(preferenceUtils: PreferenceUtils): RemoteDataStoreResult {
      val useRemoteDataStore = preferenceUtils.useRemoteDataStore
      val airtableKey = preferenceUtils.airtableApiKey
      val airtableBase = preferenceUtils.airtableBaseId
      val isRemoteDataStore = !useRemoteDataStore
              || TextUtils.isEmpty(airtableKey) || TextUtils.isEmpty(airtableBase)
      return RemoteDataStoreResult(isRemoteDataStore, airtableKey, airtableBase)
    }

    @Provides
    @Singleton
    fun providesDatabase(context: Context): DaysDatabase {
      return Room.databaseBuilder(context, DaysDatabase::class.java, DATABASE)
              .addMigrations(DaysDatabase.MIGRATION_1_2)
              .addMigrations(DaysDatabase.MIGRATION_2_3)
              .build()
    }

    @Provides
    @Singleton
    fun providesAppEventRepository(
            @Named("local") localDataStore: EventRepository,
            @Named("remote") remoteDataStore: EventRepository): AppEventRepository {
      return AppEventRepository(localDataStore, remoteDataStore)
    }

    @Provides
    @Singleton
    @Named("local")
    fun providesLocalEventRepository(preferenceUtils: PreferenceUtils,
                                     db: DaysDatabase,
                                     eventDataMapper: DbEventDataMapper): EventRepository {
      val remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils)
      return if (remoteDataStoreResult.isRemoteDataStore) {
        ReadOnlyEventRepository(db.eventDao(), eventDataMapper)
      } else {
        RoomEventRepository(db.eventDao(), eventDataMapper)
      }
    }

    @Provides
    @Singleton
    @Named("remote")
    fun providesRemoteEventRepository(context: Context,
                                      preferenceUtils: PreferenceUtils,
                                      db: DaysDatabase,
                                      dbEventDataMapper: DbEventDataMapper,
                                      apiEventDataMapper: ApiEventDataMapper): EventRepository {
      val remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils)
      return if (remoteDataStoreResult.isRemoteDataStore) {
        RoomEventRepository(db.eventDao(), dbEventDataMapper)
      } else {
        AirtableEventRepository(context, remoteDataStoreResult.airtableKey,
                remoteDataStoreResult.airtableBase, apiEventDataMapper)
      }
    }

    @Provides
    @Singleton
    fun providesAppTagRepository(@Named("local") localDataStore: TagRepository,
                                 @Named("remote") remoteDataStore: TagRepository): AppTagRepository {
      return AppTagRepository(localDataStore, remoteDataStore)
    }

    @Provides
    @Singleton
    @Named("local")
    fun providesLocalTagRepository(preferenceUtils: PreferenceUtils,
                                   db: DaysDatabase,
                                   tagDataMapper: DbTagDataMapper): TagRepository {
      val remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils)
      return if (remoteDataStoreResult.isRemoteDataStore) {
        ReadOnlyTagRepository(db.tagDao(), tagDataMapper)
      } else {
        RoomTagRepository(db.tagDao(), tagDataMapper)
      }
    }

    @Provides
    @Singleton
    @Named("remote")
    fun providesRemoteTagRepository(context: Context,
                                    preferenceUtils: PreferenceUtils,
                                    db: DaysDatabase,
                                    dbEventDataMapper: DbTagDataMapper,
                                    apiEventDataMapper: ApiTagDataMapper): TagRepository {
      val remoteDataStoreResult = checkIsRemoteDataStore(preferenceUtils)
      return if (remoteDataStoreResult.isRemoteDataStore) {
        RoomTagRepository(db.tagDao(), dbEventDataMapper)
      } else {
        AirtableTagRepository(context, remoteDataStoreResult.airtableKey,
                remoteDataStoreResult.airtableBase, apiEventDataMapper)
      }
    }
  }

}