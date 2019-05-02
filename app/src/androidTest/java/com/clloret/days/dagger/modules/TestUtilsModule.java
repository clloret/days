package com.clloret.days.dagger.modules;

import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.fakes.TestPreferenceUtils;
import com.clloret.days.fakes.TestTimeProvider;
import com.clloret.days.utils.StringResourceProviderImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

@Module
public abstract class TestUtilsModule {

  @Provides
  static EventBus providesEventBus() {

    return EventBus.getDefault();
  }

  @Binds
  abstract TimeProvider bindTimeProvider(TestTimeProvider impl);

  @Binds
  abstract PreferenceUtils bindPreferenceUtils(TestPreferenceUtils impl);

  @Binds
  abstract StringResourceProvider bindStringResourceProvider(StringResourceProviderImpl impl);

}
