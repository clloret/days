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
import org.mockito.Mockito;
import timber.log.Timber;

@Module
public class TestUseCasesModule {

//  private App app;
//
//  public TestUseCasesModule(App app) {
//    this.app = app;
//  }

  @Provides
  @Singleton
  protected GetEventsUseCase providesGetEventsUseCase(final AppDataStore dataStore) {

    return new GetEventsUseCase(dataStore);
  }

  @Provides
  @Singleton
  protected GetFilteredEventsUseCase providesGetFilteredEventsUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new GetFilteredEventsUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  protected GetTagsUseCase providesGetTagsUseCase(final AppDataStore dataStore) {

    return new GetTagsUseCase(dataStore);
  }

  @Provides
  @Singleton
  protected FavoriteEventUseCase providesFavoriteEventUseCase(final AppDataStore dataStore) {

    return new FavoriteEventUseCase(dataStore);
  }

  @Provides
  @Singleton
  protected ResetEventDateUseCase providesResetEventDateUseCase(final AppDataStore dataStore, final
  TimeProvider timeProvider, EventReminderManager eventReminderManager) {

    Timber.d("providesResetEventDateUseCase");

    return Mockito.spy(new ResetEventDateUseCase(dataStore, timeProvider, eventReminderManager));
    //return Mockito.mock(ResetEventDateUseCase.class);
  }

  @Provides
  @Singleton
  protected ToggleEventReminderUseCase providesToggleEventReminderUseCase(
      final AppDataStore dataStore, EventReminderManager eventReminderManager) {

    return new ToggleEventReminderUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  protected DeleteEventUseCase providesDeleteEventUseCase(AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    Timber.d("providesDeleteEventUseCase");

    return Mockito.spy(new DeleteEventUseCase(dataStore, eventReminderManager));
    //return Mockito.mock(DeleteEventUseCase.class);
  }

  @Provides
  @Singleton
  protected CreateEventUseCase providesCreateEventUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new CreateEventUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  protected EditEventUseCase providesEditEventUseCase(final AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    return new EditEventUseCase(dataStore, eventReminderManager);
  }

  @Provides
  @Singleton
  protected CreateTagUseCase providesCreateTagUseCase(final AppDataStore dataStore) {

    return new CreateTagUseCase(dataStore);
  }

  @Provides
  @Singleton
  protected EditTagUseCase providesEditTagUseCase(final AppDataStore dataStore) {

    return new EditTagUseCase(dataStore);
  }

  @Provides
  @Singleton
  protected DeleteTagUseCase providesDeleteTagUseCase(final AppDataStore dataStore) {

    return new DeleteTagUseCase(dataStore);
  }

}
