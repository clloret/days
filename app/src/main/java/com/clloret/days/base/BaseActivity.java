package com.clloret.days.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
