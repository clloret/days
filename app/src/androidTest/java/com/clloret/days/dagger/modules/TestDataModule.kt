package com.clloret.days.dagger.modules

import android.content.Context
import androidx.room.Room
import com.clloret.days.data.cache.CacheSource
import com.clloret.days.data.local.DaysDatabase
import com.clloret.days.data.local.entities.mapper.DbEventDataMapper
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper
import com.clloret.days.data.local.repository.RoomEventRepository
import com.clloret.days.data.local.repository.RoomTagRepository
import com.clloret.days.data.repository.AppEventRepository
import com.clloret.days.data.repository.AppTagRepository
import com.clloret.days.domain.entities.Event
import com.clloret.days.domain.entities.Tag
import com.clloret.days.domain.repository.EventRepository
import com.clloret.days.domain.repository.TagRepository
import com.clloret.test_android_common.SampleData
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class TestDataModule {

  @Binds
  abstract fun bindEventCacheSource(impl: AppEventRepository): CacheSource<Event>

  @Binds
  abstract fun bindTagCacheSource(impl: AppTagRepository): CacheSource<Tag>

  @Module
  companion object {
    @Provides
    @Singleton
    fun providesDatabase(context: Context): DaysDatabase {
      val db = Room.inMemoryDatabaseBuilder(context, DaysDatabase::class.java)
              .allowMainThreadQueries().build()

      SampleData.createEntities(db)

      return db
    }

    @Provides
    @Singleton
    fun providesAppEventRepository(
            localDataStore: EventRepository,
            remoteDataStore: EventRepository): AppEventRepository {
      return AppEventRepository(localDataStore, remoteDataStore)
    }

    @Provides
    @Singleton
    fun providesLocalEventRepository(db: DaysDatabase,
                                     eventDataMapper: DbEventDataMapper): EventRepository {
      return RoomEventRepository(db.eventDao(), eventDataMapper)
    }

    @Provides
    @Singleton
    fun providesAppTagRepository(localDataStore: TagRepository,
                                 remoteDataStore: TagRepository): AppTagRepository {
      return AppTagRepository(localDataStore, remoteDataStore)
    }

    @Provides
    @Singleton
    fun providesLocalTagRepository(db: DaysDatabase,
                                   tagDataMapper: DbTagDataMapper): TagRepository {
      return RoomTagRepository(db.tagDao(), tagDataMapper)
    }

  }

}