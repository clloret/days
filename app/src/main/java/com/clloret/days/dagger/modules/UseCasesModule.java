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
import com.clloret.days.domain.reminders.EventRemindersManager;
import com.clloret.days.domain.utils.TimeProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class UseCasesModule {

  @Provides
  @Singleton
  GetEventsUseCase providesGetEventsUseCase(final AppDataStore dataStore) {

    return new GetEventsUseCase(dataStore);
  }

  @Provides
  @Singleton
  GetFilteredEventsUseCase providesGetFilteredEventsUseCase(final AppDataStore dataStore,
      EventRemindersManager eventRemindersManager) {

    return new GetFilteredEventsUseCase(dataStore, eventRemindersManager);
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
  TimeProvider timeProvider, EventRemindersManager eventRemindersManager) {

    return new ResetEventDateUseCase(dataStore, timeProvider, eventRemindersManager);
  }

  @Provides
  @Singleton
  ToggleEventReminderUseCase providesToggleEventReminderUseCase(final AppDataStore dataStore
      , EventRemindersManager eventRemindersManager) {

    return new ToggleEventReminderUseCase(dataStore, eventRemindersManager);
  }

  @Provides
  @Singleton
  DeleteEventUseCase providesDeleteEventUseCase(final AppDataStore dataStore,
      EventRemindersManager eventRemindersManager) {

    return new DeleteEventUseCase(dataStore, eventRemindersManager);
  }

  @Provides
  @Singleton
  CreateEventUseCase providesCreateEventUseCase(final AppDataStore dataStore,
      EventRemindersManager eventRemindersManager) {

    return new CreateEventUseCase(dataStore, eventRemindersManager);
  }

  @Provides
  @Singleton
  EditEventUseCase providesEditEventUseCase(final AppDataStore dataStore,
      EventRemindersManager eventRemindersManager) {

    return new EditEventUseCase(dataStore, eventRemindersManager);
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
