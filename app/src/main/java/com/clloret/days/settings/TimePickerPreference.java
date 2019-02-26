package com.clloret.days.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;
import com.clloret.days.R;

public class TimePickerPreference extends DialogPreference {

  private int time;

  public TimePickerPreference(Context context) {

    this(context, null);
  }

  @SuppressWarnings("WeakerAccess")
  public TimePickerPreference(Context context, AttributeSet attrs) {

    this(context, attrs, 0);
  }

  @SuppressWarnings("WeakerAccess")
  public TimePickerPreference(Context context, AttributeSet attrs,
      int defStyleAttr) {

    this(context, attrs, defStyleAttr, defStyleAttr);
  }

  @SuppressWarnings("WeakerAccess")
  public TimePickerPreference(Context context, AttributeSet attrs,
      int defStyleAttr, int defStyleRes) {

    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public int getTime() {

    return time;
  }

  public void setTime(int time) {

    this.time = time;
    persistInt(time);
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {

    return a.getInt(index, 0);
  }

  @Override
  protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {

    setTime(restorePersistedValue ? getPersistedInt(time) : (int) defaultValue);
  }

  @Override
  public int getDialogLayoutResource() {

    return R.layout.preference_time_picker;
  }
}
