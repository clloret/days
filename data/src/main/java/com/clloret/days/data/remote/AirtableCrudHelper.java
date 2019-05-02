package com.clloret.days.data.remote;

import com.clloret.days.data.remote.entities.mapper.DataMapper;
import com.clloret.days.domain.entities.Identifiable;
import com.sybit.airtableandroid.Table;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public class AirtableCrudHelper<T extends Identifiable, S> {

  private static final String VIEW_NAME = "Main";

  private final Table<S> table;
  private final DataMapper<T, S> dataMapper;

  public AirtableCrudHelper(
      Table<S> table,
      DataMapper<T, S> dataMapper) {

    this.table = table;
    this.dataMapper = dataMapper;
  }

  public Single<T> getById(String id) {

    throw new UnsupportedOperationException();
  }

  public Single<List<T>> getAll(boolean refresh) {

    return table.select(VIEW_NAME)
        .map(dataMapper::toEntity);
  }

  public Maybe<T> create(T entity) {

    return table.create(dataMapper.fromEntity(entity, false))
        .map(dataMapper::toEntity);
  }

  public Maybe<T> edit(T entity) {

    return table.update(dataMapper.fromEntity(entity, true))
        .map(dataMapper::toEntity);
  }

  public Maybe<Boolean> delete(T entity) {

    final String id = entity.getId();

    return table.destroy(id);
  }

}
