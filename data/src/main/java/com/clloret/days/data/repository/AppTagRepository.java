package com.clloret.days.data.repository;

import androidx.annotation.NonNull;
import com.clloret.days.data.cache.CacheManager;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.TagRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public class AppTagRepository implements TagRepository, CacheSource<Tag> {

  private final CacheManager<Tag, TagRepository> cacheManager;
  private final CacheSource<Tag> cacheSource;

  public AppTagRepository(TagRepository localRepository, TagRepository remoteRepository) {

    cacheManager = new CacheManager<>(localRepository, remoteRepository);

    if (!(localRepository instanceof CacheSource)) {
      throw new IllegalArgumentException(
          "El argument localRepository must implement the CacheSource interface");
    }

    cacheSource = (CacheSource<Tag>) localRepository;
  }

  @Override
  public Single<Tag> getById(@NonNull String id) {

    return cacheManager.getById(id);
  }

  @Override
  public Single<List<Tag>> getAll(boolean refresh) {

    return cacheManager.getAll(refresh);
  }

  @Override
  public Maybe<Tag> create(@NonNull Tag entity) {

    return cacheManager.create(entity);
  }

  @Override
  public Maybe<Tag> edit(@NonNull Tag entity) {

    return cacheManager.edit(entity);
  }

  @Override
  public Maybe<Boolean> delete(@NonNull Tag entity) {

    return cacheManager.delete(entity);
  }

  @Override
  public void insertAll(@NonNull List<Tag> entities) {

    cacheSource.insertAll(entities);
  }

  @Override
  public Completable deleteAll() {

    return cacheSource.deleteAll();
  }
}
