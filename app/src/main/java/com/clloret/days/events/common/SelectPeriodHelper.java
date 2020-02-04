package com.clloret.days.events.common;

import android.content.res.Resources;
import androidx.fragment.app.FragmentManager;
import com.clloret.days.R;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.events.common.SelectPeriodDialog.SelectPeriodDialogListener;
import com.clloret.days.model.entities.EventViewModel;
import javax.inject.Inject;

public class SelectPeriodHelper implements SelectPeriodDialogListener {

  private final Resources resources;
  private SelectPeriodHelperListener listener;

  @Inject
  public SelectPeriodHelper(Resources resources) {

    this.resources = resources;
  }

  @Override
  public void onFinishPeriodDialog(Integer period, TimeUnit timeUnit) {

    if (listener != null) {
      listener.onResult(period, timeUnit);
    }
  }

  private void showPeriodDialog(FragmentManager fragmentManager, String title,
      boolean showTimeSelector, Integer period, TimeUnit timeUnit) {

    SelectPeriodDialog dialog = SelectPeriodDialog
        .newInstance(title, showTimeSelector, period, timeUnit, this);
    dialog.show(fragmentManager, null);
  }

  public void showSelectReminderDialog(FragmentManager fragmentManager, EventViewModel event,
      SelectPeriodHelperListener listener) {

    this.listener = listener;

    String title = resources.getString(R.string.period_select_title_set_reminder);

    showPeriodDialog(fragmentManager, title, true, event.getReminder(),
        event.getReminderUnit());
  }

  public void showSelectTimeLapseResetDialog(FragmentManager fragmentManager, EventViewModel event,
      SelectPeriodHelperListener listener) {

    this.listener = listener;

    String title = resources.getString(R.string.period_select_title_set_restart_time);

    showPeriodDialog(fragmentManager, title, false, event.getTimeLapse(),
        event.getTimeLapseUnit());
  }

  public interface SelectPeriodHelperListener {

    void onResult(Integer period, TimeUnit timeUnit);
  }

}
