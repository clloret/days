package com.clloret.days.dagger.modules;

import android.content.Context;
import android.content.res.Resources;
import com.clloret.days.Navigator;
import com.clloret.days.device.PreferenceUtilsImpl;
import com.clloret.days.device.TimeProviderImpl;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.utils.StringResourceProviderImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

@Module
public class UtilsModule {

  @Provides
  @Singleton
  EventBus providesEventBus() {

    return EventBus.getDefault();
  }

  @Provides
  @Singleton
  Navigator providesNavigator() {

    return new Navigator();
  }

  @Provides
  @Singleton
  TimeProvider providesTimeProvider() {

    return new TimeProviderImpl();
  }

  @Provides
  @Singleton
  PreferenceUtils providesPreferenceUtils(Context context,
      StringResourceProvider stringResourceProvider) {

    return new PreferenceUtilsImpl(context, stringResourceProvider);
  }

  @Provides
  @Singleton
  StringResourceProvider providesStringResourceProvider(Resources resources) {

    return new StringResourceProviderImpl(resources);
  }

}
