package com.clloret.days;

import android.content.Context;
import android.content.Intent;
import com.clloret.days.events.create.EventCreateActivity;
import com.clloret.days.events.edit.EventEditActivity;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.settings.SettingsActivity;
import com.clloret.days.tags.edit.TagEditActivity;

public class Navigator {

  public void navigateToEventCreate(Context context) {

    if (context != null) {
      Intent intentToLaunch = EventCreateActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToEventEdit(Context context, EventViewModel event) {

    if (context != null) {
      Intent intentToLaunch = EventEditActivity.getCallingIntent(context, event);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToTagsEdit(Context context, TagViewModel tag) {

    if (context != null) {
      Intent intentToLaunch = TagEditActivity.getCallingIntent(context, tag);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToSettings(Context context) {

    if (context != null) {
      Intent intentToLaunch = new Intent(context, SettingsActivity.class);
      context.startActivity(intentToLaunch);
    }
  }
}
