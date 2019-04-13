package com.clloret.days.dagger.modules;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.ResetEventDateUseCase;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.utils.TimeProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.mockito.Mockito;
import timber.log.Timber;

@Module
public class TestUseCasesModule {

  @Provides
  @Singleton
  ResetEventDateUseCase providesResetEventDateUseCase(AppDataStore dataStore,
      TimeProvider timeProvider, EventReminderManager eventReminderManager) {

    Timber.d("providesResetEventDateUseCase");

    return Mockito.spy(new ResetEventDateUseCase(dataStore, timeProvider, eventReminderManager));
  }

  @Provides
  @Singleton
  DeleteEventUseCase providesDeleteEventUseCase(AppDataStore dataStore,
      EventReminderManager eventReminderManager) {

    Timber.d("providesDeleteEventUseCase");

    return Mockito.spy(new DeleteEventUseCase(dataStore, eventReminderManager));
  }

}
