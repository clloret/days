package com.clloret.days.data.remote.repository;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.clloret.days.data.remote.AirtableCrudHelper;
import com.clloret.days.data.remote.entities.ApiEvent;
import com.clloret.days.data.remote.entities.mapper.ApiEventDataMapper;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.repository.EventRepository;
import com.sybit.airtableandroid.Airtable;
import com.sybit.airtableandroid.Base;
import com.sybit.airtableandroid.Configuration;
import com.sybit.airtableandroid.Table;
import com.sybit.airtableandroid.exception.AirtableException;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import timber.log.Timber;

public class AirtableEventRepository implements EventRepository {

  private static final String TABLE_NAME = "Events";
  private static final String VIEW_NAME = "Main";

  private Table<ApiEvent> table;
  private final ApiEventDataMapper dataMapper;
  private final AirtableCrudHelper<Event, ApiEvent> crudHelper;

  public AirtableEventRepository(Context context, String key, String database,
      ApiEventDataMapper dataMapper) {

    this.dataMapper = dataMapper;

    Configuration configuration = new Configuration(key);
    createTables(context, database, configuration);

    crudHelper = new AirtableCrudHelper<>(table, dataMapper);
  }

  public AirtableEventRepository(Context context, String endpointUrl, String key, String database,
      ApiEventDataMapper dataMapper) {

    this.dataMapper = dataMapper;

    Configuration configuration = new Configuration(key, endpointUrl);
    createTables(context, database, configuration);

    crudHelper = new AirtableCrudHelper<>(table, dataMapper);
  }

  private void createTables(Context context, String database, Configuration configuration) {

    createEventsTable(context, configuration, database);
  }

  @SuppressWarnings("unchecked")
  private void createEventsTable(Context context, Configuration configuration, String database) {

    try {
      Airtable airtable = new Airtable(context).configure(configuration);
      Base base = airtable.base(database);
      table = (Table<ApiEvent>) base.table(TABLE_NAME, ApiEvent.class);
    } catch (AirtableException e) {
      Timber.e(e, "Error creating events table");
    }
  }

  @Override
  public Single<List<Event>> getByTagId(@NonNull String tagId) {

    Single<List<ApiEvent>> listSingle;
    if (TextUtils.isEmpty(tagId)) {
      listSingle = table.select(VIEW_NAME, "{TagId} = ''");
    } else {
      listSingle = table
          .select(VIEW_NAME, String.format("OR(FIND('%s', TagId) > 0)", tagId));
    }
    return listSingle
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<List<Event>> getByFavorite() {

    return table.select(VIEW_NAME, "{Favorite} = true")
        .map(dataMapper::toEntity);
  }

  @Override
  public Single<Event> getById(String id) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Single<List<Event>> getAll(boolean refresh) {

    return crudHelper.getAll(refresh);
  }

  @Override
  public Maybe<Event> create(Event entity) {

    return crudHelper.create(entity);
  }

  @Override
  public Maybe<Event> edit(Event entity) {

    return crudHelper.edit(entity);
  }

  @Override
  public Maybe<Boolean> delete(Event entity) {

    return crudHelper.delete(entity);
  }
}
