package com.clloret.days.dagger;

import com.clloret.days.Navigator;
import com.clloret.days.device.TimeProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.greenrobot.eventbus.EventBus;

@Module
public abstract class UtilsModule {

  @Provides
  @Singleton
  public static EventBus providesEventBus() {

    return EventBus.getDefault();
  }

  @Provides
  @Singleton
  public static Navigator providesNavigator() {

    return new Navigator();
  }

  @Provides
  @Singleton
  public static TimeProvider providesTimeProvider() {

    return new TimeProvider();
  }

}
