package com.clloret.days.data.local;

import android.text.TextUtils;
import com.clloret.days.data.local.entities.dao.BaseDao;
import com.clloret.days.data.remote.entities.mapper.DataMapper;
import com.clloret.days.domain.entities.Identifiable;
import io.reactivex.Maybe;
import java.util.List;
import java.util.UUID;

public class RoomCrudHelper<T extends Identifiable, S, U extends BaseDao<S>> {

  private final U dao;
  private final DataMapper<T, S> dataMapper;

  public RoomCrudHelper(U dao, DataMapper<T, S> dataMapper) {

    this.dao = dao;
    this.dataMapper = dataMapper;
  }

  public Maybe<T> create(T entity) {

    if (TextUtils.isEmpty(entity.getId())) {
      UUID uuid = UUID.randomUUID();
      entity.setId(uuid.toString());
    }

    return Maybe.fromCallable(() -> {

      dao.insert(dataMapper.fromEntity(entity, true));
      return entity;
    });
  }

  public Maybe<T> edit(T entity) {

    return Maybe.fromCallable(() -> {

      dao.update(dataMapper.fromEntity(entity, true));
      return entity;
    });
  }

  public Maybe<Boolean> delete(T entity) {

    return Maybe.fromCallable(() -> dao.delete(dataMapper.fromEntity(entity, true)) > 0);
  }

  public void insertAll(List<T> entities) {

    dao.insertAll(dataMapper.fromEntity(entities));
  }

}
