package com.clloret.days.domain.repository;

import com.clloret.days.domain.entities.Event;
import io.reactivex.Single;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

public interface EventRepository extends GenericRepository<Event> {

  Single<List<Event>> getByTagId(@NotNull String tagId);

  Single<List<Event>> getByFavorite();

  Single<List<Event>> getBeforeDate(LocalDate date);

  Single<List<Event>> getAfterDate(LocalDate date);

  Single<List<Event>> getByDate(LocalDate date);

}
