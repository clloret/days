package com.clloret.days.base;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.clloret.days.utils.ActivityUtility;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

public abstract class BaseMvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends
    MvpActivity<V, P> {

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    injectDependencies();

    super.onCreate(savedInstanceState);

    ActivityUtility.setStatusBarColorPrimaryDark(this);
  }

  protected void configureActionBar(Toolbar toolbar) {

    ActivityUtility.configureActionBar(this, toolbar);
  }

  protected void showToastMessage(String message) {

    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
  }

  protected void showToastMessage(@StringRes int resId) {

    String message = getString(resId);
    showToastMessage(message);
  }

  protected void showSnackbarMessage(View view, String message) {

    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        .show();
  }

  protected void showSnackbarMessage(String message) {

    View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
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

  protected void showSoftKeyboard() {

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
  }

  protected abstract void injectDependencies();
}
