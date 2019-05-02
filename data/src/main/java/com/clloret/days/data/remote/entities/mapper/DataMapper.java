package com.clloret.days.data.remote.entities.mapper;

import java.util.Collection;
import java.util.List;

public interface DataMapper<T, S> {

  T toEntity(S model);

  List<T> toEntity(Collection<S> modelCollection);

  S fromEntity(T model, boolean copyId);

  List<S> fromEntity(Collection<T> modelCollection);
}
