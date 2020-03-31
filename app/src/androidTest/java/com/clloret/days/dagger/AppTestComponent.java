package com.clloret.days.dagger;

import android.app.Application;
import com.clloret.days.App;
import com.clloret.days.NotificationsTest;
import com.clloret.days.TestApp;
import com.clloret.days.activities.MainActivityTest;
import com.clloret.days.dagger.modules.ActivityModule;
import com.clloret.days.dagger.modules.BroadcastReceiverModule;
import com.clloret.days.dagger.modules.EventSortModule;
import com.clloret.days.dagger.modules.ServiceModule;
import com.clloret.days.dagger.modules.TestAppModule;
import com.clloret.days.dagger.modules.TestDataModule;
import com.clloret.days.dagger.modules.TestThreadingModule;
import com.clloret.days.dagger.modules.TestUseCasesModule;
import com.clloret.days.dagger.modules.TestUtilsModule;
import com.clloret.days.screenshots.BaseScreenshotsTest;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import java.util.Locale;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        ActivityModule.class,
        AndroidSupportInjectionModule.class,
        TestAppModule.class,
        BroadcastReceiverModule.class,
        EventSortModule.class,
        ServiceModule.class,
        TestDataModule.class,
        TestThreadingModule.class,
        TestUseCasesModule.class,
        TestUtilsModule.class
    }
)
public interface AppTestComponent extends AndroidInjector<TestApp> {

  void inject(App app);

  void inject(NotificationsTest test);

  void inject(MainActivityTest test);

  void inject(BaseScreenshotsTest test);

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(Application application);

    @BindsInstance
    Builder locale(Locale locale);

    AppTestComponent build();
  }
}