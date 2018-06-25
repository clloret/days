package com.clloret.days;

import android.content.Context;
import android.content.Intent;
import com.clloret.days.activities.SettingsActivity;
import com.clloret.days.events.create.EventCreateActivity;
import com.clloret.days.events.edit.EventEditActivity;
import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.Tag;
import com.clloret.days.tags.edit.TagEditActivity;

public class Navigator {

  public void navigateToEventCreate(Context context) {

    if (context != null) {
      Intent intentToLaunch = EventCreateActivity.getCallingIntent(context);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToEventEdit(Context context, Event event) {

    if (context != null) {
      Intent intentToLaunch = EventEditActivity.getCallingIntent(context, event);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToTagsEdit(Context context, Tag tag) {

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
