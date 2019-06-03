package com.clloret.days.events.common;

import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.clloret.days.R;
import com.clloret.days.domain.entities.Event.TimeUnit;
import java.io.Serializable;
import java.util.Objects;

public class SelectPeriodDialog extends DialogFragment {

  private static final String BUNDLE_TITLE = "title";
  private static final String BUNDLE_PERIOD = "period";
  private static final String BUNDLE_TIME_UNIT = "timeUnit";
  private static final String BUNDLE_SHOW_TIME_SELECTOR = "showTimeSelector";
  private static final String BUNDLE_LISTENER = "listener";
  private static final int PERIOD_MIN_VALUE = 0;
  private static final int PERIOD_MAX_VALUE = 100;

  public SelectPeriodDialog() {

    super();
    // Empty constructor required for DialogFragment
  }

  public static SelectPeriodDialog newInstance(String title, boolean showTimeSelector,
      Integer period, @NonNull TimeUnit timeUnit, SelectPeriodDialogListener listener) {

    Bundle args = new Bundle();

    args.putBoolean(BUNDLE_SHOW_TIME_SELECTOR, showTimeSelector);
    args.putString(BUNDLE_TITLE, title);
    args.putString(BUNDLE_TIME_UNIT, timeUnit.name());
    args.putSerializable(BUNDLE_PERIOD, period);
    args.putSerializable(BUNDLE_LISTENER, listener);

    SelectPeriodDialog dialog = new SelectPeriodDialog();
    dialog.setArguments(args);

    return dialog;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    final Bundle args = Objects.requireNonNull(getArguments());
    final boolean showTimeSelector = args.getBoolean(BUNDLE_SHOW_TIME_SELECTOR);
    final String title = args.getString(BUNDLE_TITLE);
    final TimeUnit timeUnit = TimeUnit.valueOf(args.getString(BUNDLE_TIME_UNIT));
    final SelectPeriodDialogListener listener = (SelectPeriodDialogListener) args
        .getSerializable(BUNDLE_LISTENER);
    Integer period = (Integer) args.getSerializable(BUNDLE_PERIOD);
    if (period == null) {
      period = 0;
    }

    final AlertDialog.Builder builder = new AlertDialog.Builder(Objects
        .requireNonNull(getActivity()));

    final LayoutInflater inflater = getActivity().getLayoutInflater();
    // Pass null as the parent view because its going in the dialog layout
    @SuppressLint("InflateParams") final View view = inflater
        .inflate(R.layout.dialog_period_select, null);

    final NumberPicker viewPeriod = getNumberPickerAndConfigure(view, abs(period));
    final RadioGroup viewTimeUnit = view.findViewById(R.id.radiogroup_selectperiod_timeunit);

    final int actualCheckedTimeUnit = getRadioButtonFromTimeUnit(timeUnit);
    viewTimeUnit.check(actualCheckedTimeUnit);

    final RadioGroup viewTime = view.findViewById(R.id.radiogroup_selectperiod_time);
    if (showTimeSelector) {
      viewTime.check(
          period <= 0 ? R.id.radiobutton_selectperiod_before : R.id.radiobutton_selectperiod_after);
    } else {
      viewTime.setVisibility(View.GONE);
    }

    builder.setTitle(title)
        .setView(view)
        .setPositiveButton(getString(R.string.action_ok), (dialog, id) -> {

          final int newCheckedTimeUnit = viewTimeUnit.getCheckedRadioButtonId();
          final TimeUnit newTimeUnit = getTimeUnitFromSelectedRadioButton(newCheckedTimeUnit);
          final int newPeriod =
              showTimeSelector ? getNewPeriodFromRadioButton(viewTime, viewPeriod.getValue())
                  : viewPeriod.getValue();

          if (listener != null) {
            listener.onFinishPeriodDialog(newPeriod, newTimeUnit);
          }
        })
        .setNegativeButton(getString(R.string.action_cancel), (dialog, id) -> {
          // Do nothing
        });

    return builder.create();
  }

  private int getNewPeriodFromRadioButton(final RadioGroup viewTime, final int period) {

    final int checkedTime = viewTime.getCheckedRadioButtonId();
    int newPeriod = period;
    switch (checkedTime) {
      case R.id.radiobutton_selectperiod_after:
        break;
      case R.id.radiobutton_selectperiod_before:
      default:
        newPeriod = -newPeriod;
        break;
    }
    return newPeriod;
  }

  private int getRadioButtonFromTimeUnit(TimeUnit timeUnit) {

    int actualCheckedTimeUnit;
    switch (timeUnit) {
      case MONTH:
        actualCheckedTimeUnit = R.id.radiobutton_selectperiod_month;
        break;
      case YEAR:
        actualCheckedTimeUnit = R.id.radiobutton_selectperiod_year;
        break;
      case DAY:
      default:
        actualCheckedTimeUnit = R.id.radiobutton_selectperiod_day;
        break;
    }
    return actualCheckedTimeUnit;
  }

  @NonNull
  private NumberPicker getNumberPickerAndConfigure(View view, int value) {

    NumberPicker viewPeriod = view.findViewById(R.id.picker_selectperiod_period);
    viewPeriod.setMinValue(PERIOD_MIN_VALUE);
    viewPeriod.setMaxValue(PERIOD_MAX_VALUE);
    viewPeriod.setValue(value);
    viewPeriod.setWrapSelectorWheel(true);
    viewPeriod.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    return viewPeriod;
  }

  @NonNull
  private TimeUnit getTimeUnitFromSelectedRadioButton(@IdRes int radioButtonId) {

    TimeUnit newTimeUnit;
    switch (radioButtonId) {
      case R.id.radiobutton_selectperiod_month:
        newTimeUnit = TimeUnit.MONTH;
        break;
      case R.id.radiobutton_selectperiod_year:
        newTimeUnit = TimeUnit.YEAR;
        break;
      case R.id.radiobutton_selectperiod_day:
      default:
        newTimeUnit = TimeUnit.DAY;
        break;
    }
    return newTimeUnit;
  }

  public interface SelectPeriodDialogListener extends Serializable {

    void onFinishPeriodDialog(Integer period, TimeUnit timeUnit);
  }
}
