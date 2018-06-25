package com.clloret.days.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>> extends
    MvpFragment<V, P> {

  private Unbinder unbinder;

  @Override
  public void onAttach(Activity activity) {

    injectDependencies();
    super.onAttach(activity);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    unbinder.unbind();
  }

  protected void showToastMessage(String message) {

    Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

  protected void showSnackbarMessage(View view, String message) {

    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .show();
  }

  protected void showSnackbarMessage(String message) {

    View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    showSnackbarMessage(rootView, message);
  }

  protected void showSnackbarMessage(View view, @StringRes int resId) {

    String message = getString(resId);
    showSnackbarMessage(view, message);
  }

  protected void showSnackbarMessage(@StringRes int resId) {

    String message = getString(resId);
    showSnackbarMessage(message);
  }

  protected abstract void injectDependencies();
}
