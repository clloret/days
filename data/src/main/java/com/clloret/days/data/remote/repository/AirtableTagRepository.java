package com.clloret.days.data.remote.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import com.clloret.days.data.remote.AirtableCrudHelper;
import com.clloret.days.data.remote.entities.ApiTag;
import com.clloret.days.data.remote.entities.mapper.ApiTagDataMapper;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.TagRepository;
import com.sybit.airtableandroid.Airtable;
import com.sybit.airtableandroid.Base;
import com.sybit.airtableandroid.Configuration;
import com.sybit.airtableandroid.Table;
import com.sybit.airtableandroid.exception.AirtableException;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import timber.log.Timber;

public class AirtableTagRepository implements TagRepository {

  private static final String TABLE_NAME = "Tags";

  private Table<ApiTag> table;
  private final AirtableCrudHelper<Tag, ApiTag> crudHelper;

  public AirtableTagRepository(Context context, String key, String database,
      ApiTagDataMapper dataMapper) {

    Configuration configuration = new Configuration(key);
    createTables(context, database, configuration);

    crudHelper = new AirtableCrudHelper<>(table, dataMapper);
  }

  public AirtableTagRepository(Context context, String endpointUrl, String key, String database,
      ApiTagDataMapper dataMapper) {

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
      table = (Table<ApiTag>) base.table(TABLE_NAME, ApiTag.class);
    } catch (AirtableException e) {
      Timber.e(e, "Error creating tags table");
    }
  }

  @Override
  public Single<Tag> getById(@NonNull String id) {

    throw new UnsupportedOperationException();
  }

  @Override
  public Single<List<Tag>> getAll(boolean refresh) {

    return crudHelper.getAll(refresh);
  }

  @Override
  public Maybe<Tag> create(@NonNull Tag entity) {

    return crudHelper.create(entity);
  }

  @Override
  public Maybe<Tag> edit(@NonNull Tag entity) {

    return crudHelper.edit(entity);
  }

  @Override
  public Maybe<Boolean> delete(@NonNull Tag entity) {

    return crudHelper.delete(entity);
  }
}
