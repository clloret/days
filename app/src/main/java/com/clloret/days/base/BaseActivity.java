package com.clloret.days.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.clloret.days.utils.ActivityUtility;

public abstract class BaseActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    ActivityUtility.setStatusBarColorPrimaryDark(this);
  }

  protected void configureActionBar(Toolbar toolbar) {

    ActivityUtility.configureActionBar(this, toolbar);
  }

}
