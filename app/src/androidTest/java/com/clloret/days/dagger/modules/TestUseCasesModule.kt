package com.clloret.days.dagger.modules

import com.clloret.days.domain.interactors.events.DeleteEventUseCase
import com.clloret.days.domain.interactors.events.ResetEventDateUseCase
import com.clloret.days.domain.reminders.EventReminderManager
import com.clloret.days.domain.repository.EventRepository
import com.clloret.days.domain.utils.ThreadSchedulers
import com.clloret.days.domain.utils.TimeProvider
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import timber.log.Timber

@Module
class TestUseCasesModule {
  @Provides
  fun providesResetEventDateUseCase(threadSchedulers: ThreadSchedulers,
                                    dataStore: EventRepository,
                                    timeProvider: TimeProvider,
                                    eventReminderManager: EventReminderManager): ResetEventDateUseCase {
    Timber.d("providesResetEventDateUseCase")
    return Mockito.spy(
            ResetEventDateUseCase(threadSchedulers, dataStore, timeProvider, eventReminderManager))
  }

  @Provides
  fun providesDeleteEventUseCase(threadSchedulers: ThreadSchedulers,
                                 dataStore: EventRepository,
                                 eventReminderManager: EventReminderManager): DeleteEventUseCase {
    Timber.d("providesDeleteEventUseCase")
    return Mockito.spy(DeleteEventUseCase(threadSchedulers, dataStore, eventReminderManager))
  }
}