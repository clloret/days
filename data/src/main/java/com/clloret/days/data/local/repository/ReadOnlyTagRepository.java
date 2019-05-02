package com.clloret.days.data.local.repository;

import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.data.local.entities.DbTag;
import com.clloret.days.data.local.entities.dao.TagDao;
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.TagRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public class ReadOnlyTagRepository implements TagRepository, CacheSource<Tag> {

  private final TagDao dao;
  private final DbTagDataMapper dataMapper;

  public ReadOnlyTagRepository(TagDao dao, DbTagDataMapper dataMapper) {

    this.dao = dao;
    this.dataMapper = dataMapper;
  }

  @Override
  public void insertAll(List<Tag> entities) {
    // Dummy, do nothing
  }

  @Override
  public Completable deleteAll() {

    return Completable.complete();
  }

  @Override
  public Single<Tag> getById(String id) {

    return Single.fromCallable(() -> {
      DbTag tagById = dao.getTagById(id);
      return dataMapper.toEntity(tagById);
    });
  }

  @Override
  public Single<List<Tag>> getAll(boolean refresh) {

    return dao.getAll()
        .map(dataMapper::toEntity);
  }

  @Override
  public Maybe<Tag> create(Tag entity) {

    return Maybe.just(entity);
  }

  @Override
  public Maybe<Tag> edit(Tag entity) {

    return Maybe.just(entity);
  }

  @Override
  public Maybe<Boolean> delete(Tag entity) {

    return Maybe.just(true);
  }
}
