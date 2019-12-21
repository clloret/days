package com.clloret.days.data.repository;

import androidx.annotation.NonNull;
import com.clloret.days.data.cache.CacheManager;
import com.clloret.days.data.cache.CacheSource;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import org.joda.time.LocalDate;

public class AppEventRepository implements EventRepository, CacheSource<Event> {

  private final EventRepository localRepository;
  private final CacheManager<Event, EventRepository> cacheManager;
  private final CacheSource<Event> cacheSource;

  public AppEventRepository(EventRepository localRepository, EventRepository remoteRepository) {

    this.localRepository = localRepository;
    cacheManager = new CacheManager<>(localRepository, remoteRepository);

    if (!(localRepository instanceof CacheSource)) {
      throw new IllegalArgumentException(
          "El argument localRepository must implement the CacheSource interface");
    }

    cacheSource = (CacheSource<Event>) localRepository;
  }

  @Override
  public Single<List<Event>> getByTagId(@NonNull String tagId) {

    return localRepository.getByTagId(tagId);
  }

  @Override
  public Single<List<Event>> getByFavorite() {

    return localRepository.getByFavorite();
  }

  @Override
  public Single<List<Event>> getBeforeDate(LocalDate date) {

    return localRepository.getBeforeDate(date);
  }

  @Override
  public Single<List<Event>> getAfterDate(LocalDate date) {

    return localRepository.getAfterDate(date);
  }

  @Override
  public Single<List<Event>> getByDate(LocalDate date) {

    return localRepository.getByDate(date);
  }

  @Override
  public Single<Event> getById(@NonNull String id) {

    return cacheManager.getById(id);
  }

  @Override
  public Single<List<Event>> getAll(boolean refresh) {

    return cacheManager.getAll(refresh);
  }

  @Override
  public Maybe<Event> create(@NonNull Event entity) {

    return cacheManager.create(entity);
  }

  @Override
  public Maybe<Event> edit(@NonNull Event entity) {

    return cacheManager.edit(entity);
  }

  @Override
  public Maybe<Boolean> delete(@NonNull Event entity) {

    return cacheManager.delete(entity);
  }

  @Override
  public void insertAll(@NonNull List<Event> entities) {

    cacheSource.insertAll(entities);
  }

  @Override
  public Completable deleteAll() {

    return cacheSource.deleteAll();
  }

}
