package com.clloret.days.dagger.modules

import com.clloret.days.device.PreferenceUtilsImpl
import com.clloret.days.domain.utils.PreferenceUtils
import com.clloret.days.domain.utils.StringResourceProvider
import com.clloret.days.domain.utils.TimeProvider
import com.clloret.days.fakes.TestTimeProvider
import com.clloret.days.utils.StringResourceProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus

@Module
abstract class TestUtilsModule {

  @Binds
  abstract fun bindTimeProvider(impl: TestTimeProvider): TimeProvider

  @Binds
  abstract fun bindPreferenceUtils(impl: PreferenceUtilsImpl): PreferenceUtils

  @Binds
  abstract fun bindStringResourceProvider(impl: StringResourceProviderImpl): StringResourceProvider

  @Module
  companion object {

    @Provides
    fun providesEventBus(): EventBus = EventBus.getDefault()

  }

}