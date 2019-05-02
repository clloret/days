package com.clloret.days.data.cache;

import io.reactivex.Completable;
import java.util.List;

public interface CacheSource<T> {

  void insertAll(List<T> entities);

  Completable deleteAll();

}
