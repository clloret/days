package com.clloret.days.data.cache;

import com.clloret.days.domain.repository.GenericRepository;
import com.sybit.airtableandroid.exception.AirtableException;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class CacheManager<T, R extends GenericRepository<T>> implements GenericRepository<T> {

  private final R localRepository;
  private final R remoteRepository;
  private final CacheSource<T> cacheSource;

  public CacheManager(R localRepository, R remoteRepository) {

    this.localRepository = localRepository;
    this.remoteRepository = remoteRepository;

    if (!(localRepository instanceof CacheSource)) {
      throw new IllegalArgumentException(
          "El argument localRepository must implement the CacheSource interface");
    }

    cacheSource = (CacheSource<T>) localRepository;
  }

  private Single<List<T>> getAllFromLocal() {

    return localRepository.getAll(false)
        .doOnSuccess(entities -> Timber.d("Local data successfully obtained"));
  }

  private Single<List<T>> getAllFromRemote() {

    return remoteRepository.getAll(false)
        .doFinally(() -> Timber.d("Remote data successfully obtained"))
        .flatMap(this::storeRemoteDataInLocal);
  }

  private Single<List<T>> storeRemoteDataInLocal(List<T> entities) {

    return Single.fromCallable(() -> {
      cacheSource.deleteAll();
      cacheSource.insertAll(entities);

      return entities;
    }).doFinally(() -> Timber.d("Inserted the remote data in local..."));
  }

  @Override
  public Single<T> getById(String id) {

    return localRepository.getById(id);
  }

  @Override
  public Single<List<T>> getAll(boolean refresh) {

    if (refresh) {
      return getAllFromRemote();
    } else {
      return Single.concat(
          getAllFromLocal(),
          getAllFromRemote())
          .filter(entities -> !entities.isEmpty())
          .first(new ArrayList<>());
    }
  }

  @Override
  public Maybe<T> create(T entity) {

    // Use "result" instead of "date" because the "id" is assigned in "result".
    return remoteRepository.create(entity)
        .doOnSuccess(result -> localRepository.create(result)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }

  @Override
  public Maybe<T> edit(T entity) {

    return remoteRepository.edit(entity)
        .doOnSuccess(result -> localRepository.edit(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }

  @Override
  public Maybe<Boolean> delete(T entity) {

    return remoteRepository.delete(entity)
        .onErrorReturn(this::checkAirtableNotFoundError)
        .doOnSuccess(result -> {
          if (result) {
            localRepository.delete(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
          }
        });
  }

  private boolean checkAirtableNotFoundError(Throwable error) {

    if (error instanceof AirtableException) {
      AirtableException airtableException = (AirtableException) error;

      int statusCode = airtableException.getStatusCode();
      Timber.d("StatusCode: %d, Message: %s", statusCode,
          airtableException.getMessage());

      if (statusCode == HttpURLConnection.HTTP_NOT_FOUND) {
        Timber.w("Orphaned record, delete local record");

        return true;
      }
    }

    return false;
  }

}
