package com.clloret.days.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
