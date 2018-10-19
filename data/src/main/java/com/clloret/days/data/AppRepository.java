package com.clloret.days.data;

import android.support.annotation.NonNull;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class AppRepository implements AppDataStore {

  private AppDataStore localDataStore;
  private AppDataStore remoteDataStore;

  @Inject
  public AppRepository(AppDataStore localDataStore,
      AppDataStore remoteDataStore) {

    this.localDataStore = localDataStore;
    this.remoteDataStore = remoteDataStore;
  }

  private void storeEventsInDb(List<Event> events) {

    Observable.fromCallable(() -> {
      localDataStore.deleteAllEvents();
      localDataStore.insertAllEvents(events);
      return true;
    })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(t -> Timber.d("Inserted events from API in DB..."));
  }

  private Single<List<Event>> getEventsFromLocal() {

    return localDataStore.getEvents(false)
        .doOnSuccess(events -> Timber.d("Local Events"));
  }

  private Single<List<Event>> getEventsFromRemote() {

    return remoteDataStore.getEvents(false)
        .doOnSuccess(events -> {
          Timber.d("Remote Events");
          storeEventsInDb(events);
        });
  }

  private void storeTagsInDb(List<Tag> tags) {

    Observable.fromCallable(() -> {
      localDataStore.deleteAllTags();
      localDataStore.insertAllTags(tags);
      return true;
    })
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(t -> Timber.d("Inserted tags from API in DB..."));
  }

  private Single<List<Tag>> getTagsFromLocal() {

    return localDataStore.getTags(false)
        .doOnSuccess(events -> Timber.d("Local Tags"));
  }

  private Single<List<Tag>> getTagsFromRemote() {

    return remoteDataStore.getTags(false)
        .doOnSuccess(tags -> {
          Timber.d("Remote Tags");
          storeTagsInDb(tags);
        });
  }

  @Override
  public void insertAllEvents(List<Event> events) {

    throw new UnsupportedOperationException();
  }

  @Override
  public void insertAllTags(List<Tag> tags) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Completable deleteAllEvents() {

    throw new UnsupportedOperationException();
  }

  @Override
  public Completable deleteAllTags() {

    throw new UnsupportedOperationException();
  }

  @Override
  public Completable invalidateAll() {

    return localDataStore.invalidateAll()
        .andThen(remoteDataStore.invalidateAll());
  }

  @Override
  public Single<List<Event>> getEvents(boolean refresh) {

    if (refresh) {
      return getEventsFromRemote();
    } else {
      return Single.concat(
          getEventsFromLocal(),
          getEventsFromRemote())
          .filter(events -> !events.isEmpty())
          .first(new ArrayList<>());
    }
  }

  @Override
  public Single<List<Event>> getEventsByTagId(@NonNull String tagId) {

    return localDataStore.getEventsByTagId(tagId);
  }

  @Override
  public Single<List<Event>> getEventsByFavorite() {

    return localDataStore.getEventsByFavorite();
  }

  @Override
  public Maybe<Event> createEvent(@NonNull Event event) {

    // Use "result" instead of "date" because the "id" is assigned in "result".
    return remoteDataStore.createEvent(event)
        .doOnSuccess(result -> localDataStore.createEvent(result)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }

  @Override
  public Maybe<Event> editEvent(@NonNull Event event) {

    return remoteDataStore.editEvent(event)
        .doOnSuccess(result -> localDataStore.editEvent(event)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }

  @Override
  public Maybe<Boolean> deleteEvent(@NonNull Event event) {

    return remoteDataStore.deleteEvent(event)
        .doOnSuccess(result -> localDataStore.deleteEvent(event)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }

  @Override
  public Single<List<Tag>> getTags(boolean refresh) {

    if (refresh) {
      return getTagsFromRemote();
    } else {
      return Single.concat(
          getTagsFromLocal(),
          getTagsFromRemote())
          .filter(tags -> !tags.isEmpty())
          .first(new ArrayList<>());
    }
  }

  @Override
  public Maybe<Tag> createTag(@NonNull Tag tag) {

    return remoteDataStore.createTag(tag)
        .doOnSuccess(result -> localDataStore.createTag(result)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }

  @Override
  public Maybe<Tag> editTag(@NonNull Tag tag) {

    return remoteDataStore.editTag(tag)
        .doOnSuccess(result -> localDataStore.editTag(result)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }

  @Override
  public Maybe<Boolean> deleteTag(@NonNull Tag tag) {

    return remoteDataStore.deleteTag(tag)
        .doOnSuccess(result -> localDataStore.deleteTag(tag)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe());
  }
}
