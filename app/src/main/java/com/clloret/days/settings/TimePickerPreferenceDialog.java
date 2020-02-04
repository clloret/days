package com.clloret.days.settings;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;
import com.clloret.days.R;
import org.joda.time.DateTimeConstants;

public class TimePickerPreferenceDialog extends PreferenceDialogFragmentCompat {

  private TimePicker timePicker;

  @Override
  protected void onBindDialogView(View view) {

    super.onBindDialogView(view);

    timePicker = view.findViewById(R.id.timePicker);

    if (timePicker == null) {
      throw new IllegalStateException("Dialog view must contain a TimePicker with id 'timePicker'");
    }

    Integer minutesAfterMidnight = null;
    DialogPreference preference = getPreference();
    if (preference instanceof TimePickerPreference) {
      minutesAfterMidnight = ((TimePickerPreference) preference).getTime();
    }

    if (minutesAfterMidnight != null) {
      int hours = minutesAfterMidnight / DateTimeConstants.MINUTES_PER_HOUR;
      int minutes = minutesAfterMidnight % DateTimeConstants.MINUTES_PER_HOUR;
      boolean is24hour = DateFormat.is24HourFormat(getContext());

      timePicker.setIs24HourView(is24hour);
      timePicker.setHour(hours);
      timePicker.setMinute(minutes);
    }
  }

  @Override
  public void onDialogClosed(boolean positiveResult) {

    if (!positiveResult) {
      return;
    }

    int hours = timePicker.getHour();
    int minutes = timePicker.getMinute();
    int minutesAfterMidnight = (hours * DateTimeConstants.MINUTES_PER_HOUR) + minutes;

    DialogPreference preference = getPreference();
    if (preference instanceof TimePickerPreference) {
      TimePickerPreference timePreference = ((TimePickerPreference) preference);
      if (timePreference.callChangeListener(minutesAfterMidnight)) {
        timePreference.setTime(minutesAfterMidnight);
      }
    }
  }

  public static TimePickerPreferenceDialog newInstance(String key) {

    final TimePickerPreferenceDialog fragment = new TimePickerPreferenceDialog();
    final Bundle bundle = new Bundle(1);

    bundle.putString(ARG_KEY, key);
    fragment.setArguments(bundle);

    return fragment;
  }
}
