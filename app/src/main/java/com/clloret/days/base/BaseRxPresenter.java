package com.clloret.days.base;

import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseRxPresenter<V extends MvpView> extends MvpNullObjectBasePresenter<V> {

  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Override
  public void detachView(boolean retainInstance) {

    super.detachView(retainInstance);
    compositeDisposable.dispose();
  }

  protected void addDisposable(@NonNull Disposable disposable) {

    compositeDisposable.add(disposable);
  }
}
