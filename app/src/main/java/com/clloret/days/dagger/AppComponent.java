package com.clloret.days.dagger;

import android.app.Application;
import com.clloret.days.App;
import com.clloret.days.dagger.modules.ActivityModule;
import com.clloret.days.dagger.modules.AppModule;
import com.clloret.days.dagger.modules.BroadcastReceiverModule;
import com.clloret.days.dagger.modules.DataModule;
import com.clloret.days.dagger.modules.EventSortModule;
import com.clloret.days.dagger.modules.ServiceModule;
import com.clloret.days.dagger.modules.ThreadingModule;
import com.clloret.days.dagger.modules.UtilsModule;
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
        DataModule.class,
        EventSortModule.class,
        ServiceModule.class,
        ThreadingModule.class,
        UtilsModule.class
    }
)
public interface AppComponent extends AndroidInjector<DaggerApplication> {

  @Override
  void inject(DaggerApplication instance);

  void inject(App app);

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(Application application);

    AppComponent build();
  }
}