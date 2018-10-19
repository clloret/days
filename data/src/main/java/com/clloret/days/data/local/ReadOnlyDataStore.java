package com.clloret.days.data.local;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.days.data.local.entities.mapper.DbEventDataMapper;
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public class ReadOnlyDataStore implements AppDataStore {

  private EventDao eventDao;
  private TagDao tagDao;
  private DbEventDataMapper eventDataMapper = new DbEventDataMapper();
  private DbTagDataMapper tagDataMapper = new DbTagDataMapper();

  public ReadOnlyDataStore(DaysDatabase db) {

    eventDao = db.eventDao();
    tagDao = db.tagDao();
  }

  @Override
  public void insertAllEvents(List<Event> events) {
    // Dummy, do nothing
  }

  @Override
  public void insertAllTags(List<Tag> tags) {
    // Dummy, do nothing
  }

  @Override
  public Completable deleteAllEvents() {

    return Completable.complete();
  }

  @Override
  public Completable deleteAllTags() {

    return Completable.complete();
  }

  @Override
  public Completable invalidateAll() {

    return Completable.complete();
  }

  @Override
  public Single<List<Event>> getEvents(boolean refresh) {

    return eventDao.getAll()
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Single<List<Event>> getEventsByTagId(@NonNull String tagId) {

    Single<List<DbEvent>> listSingle = TextUtils.isEmpty(tagId) ? eventDao.loadWithoutAssignedTags()
        : eventDao.loadByTagsIds("%" + tagId + "%");

    return listSingle
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Single<List<Event>> getEventsByFavorite() {

    return eventDao.loadFavorites()
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Maybe<Event> createEvent(@NonNull Event event) {

    return Maybe.just(event);
  }

  @Override
  public Maybe<Event> editEvent(@NonNull Event event) {

    return Maybe.just(event);
  }

  @Override
  public Maybe<Boolean> deleteEvent(@NonNull Event event) {

    return Maybe.just(true);
  }

  @Override
  public Single<List<Tag>> getTags(boolean refresh) {

    return tagDao.getAll()
        .map(tagDataMapper::toTag);
  }

  @Override
  public Maybe<Tag> createTag(@NonNull Tag tag) {

    return Maybe.just(tag);
  }

  @Override
  public Maybe<Tag> editTag(@NonNull Tag tag) {

    return Maybe.just(tag);
  }

  @Override
  public Maybe<Boolean> deleteTag(@NonNull Tag tag) {

    return Maybe.just(true);
  }
}
