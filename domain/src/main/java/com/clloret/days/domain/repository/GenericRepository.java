package com.clloret.days.domain.repository;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import java.util.List;

public interface GenericRepository<T> {

  Single<T> getById(@NonNull String id);

  Single<List<T>> getAll(boolean refresh);

  Maybe<T> create(@NonNull T entity);

  Maybe<T> edit(@NonNull T entity);

  Maybe<Boolean> delete(@NonNull T entity);
}
