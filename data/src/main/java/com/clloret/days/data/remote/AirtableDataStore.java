package com.clloret.days.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.clloret.days.data.remote.entities.ApiEvent;
import com.clloret.days.data.remote.entities.ApiTag;
import com.clloret.days.data.remote.entities.mapper.ApiEventDataMapper;
import com.clloret.days.data.remote.entities.mapper.ApiTagDataMapper;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.Tag;
import com.sybit.airtableandroid.Airtable;
import com.sybit.airtableandroid.Base;
import com.sybit.airtableandroid.Configuration;
import com.sybit.airtableandroid.Table;
import com.sybit.airtableandroid.exception.AirtableException;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import timber.log.Timber;

public class AirtableDataStore implements AppDataStore {

  private static final String TAGS_TABLE = "Tags";
  private static final String TAGS_VIEW = "Main";
  private static final String EVENTS_TABLE = "Events";
  private static final String EVENTS_VIEW = "Main";

  private Table<ApiEvent> eventTable;
  private Table<ApiTag> tagTable;
  private ApiEventDataMapper eventDataMapper = new ApiEventDataMapper();
  private ApiTagDataMapper tagDataMapper = new ApiTagDataMapper();

  public AirtableDataStore(Context context, String key, String database) {

    Configuration configuration = new Configuration(key);
    createTables(context, database, configuration);
  }

  public AirtableDataStore(Context context, String endpointUrl, String key, String database) {

    Configuration configuration = new Configuration(key, endpointUrl);
    createTables(context, database, configuration);
  }

  private void createTables(Context context, String database, Configuration configuration) {

    createEventsTable(context, configuration, database);
    createTagsTable(context, configuration, database);
  }

  @SuppressWarnings("unchecked")
  private void createEventsTable(Context context, Configuration configuration, String database) {

    try {
      Airtable airtable = new Airtable(context).configure(configuration);
      Base base = airtable.base(database);
      eventTable = (Table<ApiEvent>) base.table(EVENTS_TABLE, ApiEvent.class);
    } catch (AirtableException e) {
      Timber.e(e, "Error creating event");
    }
  }

  @SuppressWarnings("unchecked")
  private void createTagsTable(Context context, Configuration configuration, String database) {

    try {
      Airtable airtable = new Airtable(context).configure(configuration);
      Base base = airtable.base(database);
      tagTable = (Table<ApiTag>) base.table(TAGS_TABLE, ApiTag.class);
    } catch (AirtableException e) {
      Timber.e(e, "Error creating tag");
    }
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
  public Single<Event> getEventById(String eventId) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Single<List<Event>> getEvents(boolean refresh) {

    return eventTable.select(EVENTS_VIEW)
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Single<List<Event>> getEventsByTagId(@NonNull String tagId) {

    Single<List<ApiEvent>> listSingle;
    if (TextUtils.isEmpty(tagId)) {
      listSingle = eventTable.select(EVENTS_VIEW, "{TagId} = ''");
    } else {
      listSingle = eventTable
          .select(EVENTS_VIEW, String.format("OR(FIND('%s', TagId) > 0)", tagId));
    }
    return listSingle
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Single<List<Event>> getEventsByFavorite() {

    return eventTable.select(EVENTS_VIEW, "{Favorite} = true")
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Maybe<Event> createEvent(@NonNull Event event) {

    return eventTable.create(eventDataMapper.fromEvent(event, false))
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Maybe<Event> editEvent(@NonNull Event event) {

    return eventTable.update(eventDataMapper.fromEvent(event, true))
        .map(eventDataMapper::toEvent);
  }

  @Override
  public Maybe<Boolean> deleteEvent(@NonNull Event event) {

    final String id = event.getId();

    return eventTable.destroy(id);
  }

  @Override
  public Single<List<Tag>> getTags(boolean refresh) {

    return tagTable.select(TAGS_VIEW)
        .map(tagDataMapper::toTag);
  }

  @Override
  public Maybe<Tag> createTag(@NonNull Tag tag) {

    return tagTable.create(tagDataMapper.fromTag(tag, false))
        .map(tagDataMapper::toTag);
  }

  @Override
  public Maybe<Tag> editTag(@NonNull Tag tag) {

    return tagTable.update(tagDataMapper.fromTag(tag, true))
        .map(tagDataMapper::toTag);
  }

  @Override
  public Maybe<Boolean> deleteTag(@NonNull Tag tag) {

    final String id = tag.getId();

    return tagTable.destroy(id);
  }

}
