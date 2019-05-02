package com.clloret.days.domain.repository;

import com.clloret.days.domain.entities.Event;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import java.util.List;

public interface EventRepository extends GenericRepository<Event> {

  Single<List<Event>> getByTagId(@NonNull String tagId);

  Single<List<Event>> getByFavorite();

}
