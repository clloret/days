package com.clloret.days.domain.repository;

import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface GenericRepository<T> {

  Single<T> getById(@NotNull String id);

  Single<List<T>> getAll(boolean refresh);

  Maybe<T> create(@NotNull T entity);

  Maybe<T> edit(@NotNull T entity);

  Maybe<Boolean> delete(@NotNull T entity);
}
