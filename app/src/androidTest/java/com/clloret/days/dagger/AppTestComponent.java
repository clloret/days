package com.clloret.days.dagger;

import android.app.Application;
import com.clloret.days.App;
import com.clloret.days.NotificationTests;
import com.clloret.days.activities.MainActivityTest;
import com.clloret.days.dagger.modules.ActivityModule;
import com.clloret.days.dagger.modules.AppModule;
import com.clloret.days.dagger.modules.BroadcastReceiverModule;
import com.clloret.days.dagger.modules.EventSortModule;
import com.clloret.days.dagger.modules.ServiceModule;
import com.clloret.days.dagger.modules.TestDataModule;
import com.clloret.days.dagger.modules.TestUseCasesModule;
import com.clloret.days.dagger.modules.TestUtilsModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        ActivityModule.class,
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BroadcastReceiverModule.class,
        TestDataModule.class,
        EventSortModule.class,
        ServiceModule.class,
        TestUseCasesModule.class,
        TestUtilsModule.class
    }
)
public interface AppTestComponent extends AndroidInjector<DaggerApplication> {

  void inject(App app);

  void inject(NotificationTests test);

  void inject(MainActivityTest test);

  @Override
  void inject(DaggerApplication instance);

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(Application application);

    AppTestComponent build();
  }
}