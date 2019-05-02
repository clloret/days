package com.clloret.days.dagger.modules;

import com.clloret.days.device.PreferenceUtilsImpl;
import com.clloret.days.device.TimeProviderImpl;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.utils.StringResourceProviderImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

@Module
public abstract class UtilsModule {

  @Provides
  static EventBus providesEventBus() {

    return EventBus.getDefault();
  }

  @Binds
  abstract TimeProvider bindTimeProvider(TimeProviderImpl impl);

  @Binds
  abstract PreferenceUtils bindPreferenceUtils(PreferenceUtilsImpl impl);

  @Binds
  abstract StringResourceProvider bindStringResourceProvider(StringResourceProviderImpl impl);

}
