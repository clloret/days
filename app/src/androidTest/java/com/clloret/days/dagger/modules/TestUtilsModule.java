package com.clloret.days.dagger.modules;

import android.content.res.Resources;
import com.clloret.days.Navigator;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.fakes.TestPreferenceUtils;
import com.clloret.days.fakes.TestTimeProvider;
import com.clloret.days.utils.StringResourceProviderImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

@Module
public class TestUtilsModule {

  @Provides
  @Singleton
  public EventBus providesEventBus() {

    return EventBus.getDefault();
  }

  @Provides
  @Singleton
  public Navigator providesNavigator() {

    return new Navigator();
  }

  @Provides
  @Singleton
  public TimeProvider providesTimeProvider() {

    return new TestTimeProvider();
  }

  @Provides
  @Singleton
  public PreferenceUtils providesPreferenceUtils() {

    return new TestPreferenceUtils();
  }

  @Provides
  @Singleton
  public StringResourceProvider providesStringResourceProvider(Resources resources) {

    return new StringResourceProviderImpl(resources);
  }

}
