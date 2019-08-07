package com.clloret.days.data.cache;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import java.util.List;

public interface CacheSource<T> {

  void insertAll(@NonNull List<T> entities);

  Completable deleteAll();

}
