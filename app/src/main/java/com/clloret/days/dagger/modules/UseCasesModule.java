package com.clloret.days.dagger.modules;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.EditEventUseCase;
import com.clloret.days.domain.interactors.events.FavoriteEventUseCase;
import com.clloret.days.domain.interactors.events.GetEventsUseCase;
import com.clloret.days.domain.interactors.events.GetFilteredEventsUseCase;
import com.clloret.days.domain.interactors.events.ResetEventDateUseCase;
import com.clloret.days.domain.interactors.events.ToggleEventReminderUseCase;
import com.clloret.days.domain.interactors.tags.CreateTagUseCase;
import com.clloret.days.domain.interactors.tags.DeleteTagUseCase;
import com.clloret.days.domain.interactors.tags.EditTagUseCase;
import com.clloret.days.domain.interactors.tags.GetTagsUseCase;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.utils.TimeProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class UseCasesModule {

  @Provides
  @Singleton
  GetEventsUseCase providesGetEventsUseCase(final AppDataStore dataStore) {

    return new GetEventsUseCase(dataStore);
  }

  @Provides
  @Singleton
  GetFilteredEventsUseCase providesGetFilteredEventsUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new GetFilteredEventsUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  GetTagsUseCase providesGetTagsUseCase(final AppDataStore dataStore) {

    return new GetTagsUseCase(dataStore);
  }

  @Provides
  @Singleton
  FavoriteEventUseCase providesFavoriteEventUseCase(final AppDataStore dataStore) {

    return new FavoriteEventUseCase(dataStore);
  }

  @Provides
  @Singleton
  ResetEventDateUseCase providesResetEventDateUseCase(final AppDataStore dataStore, final
  TimeProvider timeProvider, EventReminderManager eventReminderManager) {

    return new ResetEventDateUseCase(dataStore, timeProvider, eventReminderManager);
  }

  @Provides
  @Singleton
  ToggleEventReminderUseCase providesToggleEventReminderUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new ToggleEventReminderUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  DeleteEventUseCase providesDeleteEventUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new DeleteEventUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  CreateEventUseCase providesCreateEventUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new CreateEventUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  EditEventUseCase providesEditEventUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new EditEventUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  CreateTagUseCase providesCreateTagUseCase(final AppDataStore dataStore) {

    return new CreateTagUseCase(dataStore);
  }

  @Provides
  @Singleton
  EditTagUseCase providesEditTagUseCase(final AppDataStore dataStore) {

    return new EditTagUseCase(dataStore);
  }

  @Provides
  @Singleton
  DeleteTagUseCase providesDeleteTagUseCase(final AppDataStore dataStore) {

    return new DeleteTagUseCase(dataStore);
  }

}
