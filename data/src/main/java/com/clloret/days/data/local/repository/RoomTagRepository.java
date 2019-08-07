package com.clloret.days.data.local.repository;

import androidx.annotation.NonNull;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.data.local.RoomCrudHelper;
import com.clloret.days.data.local.entities.DbTag;
import com.clloret.days.data.local.entities.dao.BaseDao;
import com.clloret.days.data.local.entities.dao.TagDao;
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.TagRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public class RoomTagRepository implements TagRepository, CacheSource<Tag> {

  private final TagDao dao;
  private final DbTagDataMapper dataMapper;
  private final RoomCrudHelper<Tag, DbTag, BaseDao<DbTag>> crudHelper;

  public RoomTagRepository(TagDao dao, DbTagDataMapper dataMapper) {

    this.dao = dao;
    this.dataMapper = dataMapper;
    crudHelper = new RoomCrudHelper<>(dao, dataMapper);
  }

  @Override
  public void insertAll(@NonNull List<Tag> entities) {

    crudHelper.insertAll(entities);
  }

  @Override
  public Completable deleteAll() {

    return Completable
        .fromAction(dao::deleteAll);
  }

  @Override
  public Single<Tag> getById(@NonNull String id) {

    return Single.fromCallable(() -> {
      DbTag eventById = dao.getTagById(id);
      return dataMapper.toEntity(eventById);
    });
  }

  @Override
  public Single<List<Tag>> getAll(boolean refresh) {

    return dao.getAll()
        .map(dataMapper::toEntity);
  }

  @Override
  public Maybe<Tag> create(@NonNull Tag entity) {

    return crudHelper.create(entity);
  }

  @Override
  public Maybe<Tag> edit(@NonNull Tag entity) {

    return crudHelper.edit(entity);
  }

  @Override
  public Maybe<Boolean> delete(@NonNull Tag entity) {

    return crudHelper.delete(entity);
  }
}
