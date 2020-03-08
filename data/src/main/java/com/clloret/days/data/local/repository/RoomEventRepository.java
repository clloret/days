package com.clloret.days.data.local.repository;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.data.local.RoomCrudHelper;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.dao.BaseDao;
import com.clloret.days.data.local.entities.dao.EventDao;
import com.clloret.days.data.local.entities.mapper.DbEventDataMapper;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import org.joda.time.LocalDate;

public class RoomEventRepository implements EventRepository, CacheSource<Event> {

  private final EventDao dao;
  private final DbEventDataMapper dataMapper;
  private final RoomCrudHelper<Event, DbEvent, BaseDao<DbEvent>> crudHelper;

  public RoomEventRepository(EventDao dao, DbEventDataMapper dataMapper) {

    this.dao = dao;
    this.dataMapper = dataMapper;
    crudHelper = new RoomCrudHelper<>(dao, dataMapper);
  }

  @Override
  public Single<List<Event>> getByTagId(@NonNull String tagId) {

    Single<List<DbEvent>> listSingle = TextUtils.isEmpty(tagId) ? dao.loadWithoutAssignedTags()
        : dao.loadByTagsIds("%" + tagId + "%");

    return listSingle
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<List<Event>> getByFavorite() {

    return dao.loadFavorites()
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<List<Event>> getBeforeDate(LocalDate date) {

    return dao.loadBeforeDate(date.toDate().getTime())
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<List<Event>> getAfterDate(LocalDate date) {

    return dao.loadAfterDate(date.toDate().getTime())
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<List<Event>> getByDate(LocalDate date) {

    return dao.loadByDate(date.toDate().getTime())
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<List<Event>> getByReminder() {

    return dao.loadWithReminder()
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<Event> getById(@NonNull String id) {

    return Single.fromCallable(() -> {
      DbEvent eventById = dao.getEventById(id);
      return dataMapper.toEntity(eventById);
    });
  }

  @Override
  public Single<List<Event>> getAll(boolean refresh) {

    return dao.getAll()
        .map(dataMapper::toEntity);
  }

  @Override
  public Maybe<Event> create(@NonNull Event entity) {

    return crudHelper.create(entity);
  }

  @Override
  public Maybe<Event> edit(@NonNull Event entity) {

    return crudHelper.edit(entity);
  }

  @Override
  public Maybe<Boolean> delete(@NonNull Event entity) {

    return crudHelper.delete(entity);
  }

  @Override
  public Completable deleteAll() {

    return Completable
        .fromAction(dao::deleteAll);
  }

  @Override
  public void insertAll(@NonNull List<Event> entities) {

    crudHelper.insertAll(entities);
  }
}
