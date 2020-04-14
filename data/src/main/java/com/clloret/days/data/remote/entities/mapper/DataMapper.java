package com.clloret.days.data.remote.entities.mapper;

import androidx.annotation.NonNull;
import java.util.Collection;
import java.util.List;

public interface DataMapper<T, S> {

  T toEntity(@NonNull S entity);

  List<T> toEntity(@NonNull Collection<S> entityCollection);

  S fromEntity(@NonNull T entity, boolean copyId);

  List<S> fromEntity(@NonNull Collection<T> entityCollection);
}
