package com.clloret.days.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;
import com.clloret.days.R;

public class TimePickerPreference extends DialogPreference {

  private int time;

  public TimePickerPreference(Context context) {

    this(context, null);
  }

  public TimePickerPreference(Context context, AttributeSet attrs) {

    this(context, attrs, 0);
  }

  public TimePickerPreference(Context context, AttributeSet attrs,
      int defStyleAttr) {

    this(context, attrs, defStyleAttr, defStyleAttr);
  }

  public TimePickerPreference(Context context, AttributeSet attrs,
      int defStyleAttr, int defStyleRes) {

    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {

    return a.getInt(index, 0);
  }

  @Override
  protected void onSetInitialValue(@Nullable Object defaultValue) {

    setTime(getPersistedInt(time));
  }

  @Override
  public int getDialogLayoutResource() {

    return R.layout.preference_time_picker;
  }

  public int getTime() {

    return time;
  }

  public void setTime(int time) {

    this.time = time;
    persistInt(time);
  }
}
