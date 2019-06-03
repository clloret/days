package com.clloret.days.utils;

import android.app.Activity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.clloret.days.R;

public class ActivityUtility {

  public static void configureActionBar(AppCompatActivity activity, Toolbar toolbar) {

    activity.setSupportActionBar(toolbar);
    ActionBar actionBar = activity.getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
  }

  public static void setStatusBarColorPrimaryDark(Activity activity) {

    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.primary_dark));
  }
}
