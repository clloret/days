package com.clloret.days.dagger;

import android.app.Application;
import com.clloret.days.App;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(
    modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class,
        DataModule.class,
        UtilsModule.class,
        EventSortModule.class
    }
)
public interface AppComponent extends AndroidInjector<DaggerApplication> {

  void inject(App app);

  @Override
  void inject(DaggerApplication instance);

  @Component.Builder
  interface Builder {

    @BindsInstance
    Builder application(Application application);

    AppComponent build();
  }
}