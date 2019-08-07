package com.clloret.days.dagger.modules;

import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.ResetEventDateUseCase;
import com.clloret.days.domain.reminders.EventReminderManager;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.ThreadSchedulers;
import com.clloret.days.domain.utils.TimeProvider;
import dagger.Module;
import dagger.Provides;
import org.mockito.Mockito;
import timber.log.Timber;

@Module
public class TestUseCasesModule {

  @Provides
  ResetEventDateUseCase providesResetEventDateUseCase(ThreadSchedulers threadSchedulers,
      EventRepository dataStore, TimeProvider timeProvider,
      EventReminderManager eventReminderManager) {

    Timber.d("providesResetEventDateUseCase");

    return Mockito.spy(
        new ResetEventDateUseCase(threadSchedulers, dataStore, timeProvider, eventReminderManager));
  }

  @Provides
  DeleteEventUseCase providesDeleteEventUseCase(ThreadSchedulers threadSchedulers,
      EventRepository dataStore, EventReminderManager eventReminderManager) {

    Timber.d("providesDeleteEventUseCase");

    return Mockito.spy(new DeleteEventUseCase(threadSchedulers, dataStore, eventReminderManager));
  }

}
