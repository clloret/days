package com.clloret.days.events.create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.events.common.PeriodTextFormatter;
import com.clloret.days.events.common.SelectDateHelper;
import com.clloret.days.events.common.SelectPeriodHelper;
import com.clloret.days.events.common.SelectTagsDialog.SelectTagsDialogListener;
import com.clloret.days.events.common.SelectTagsHelper;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.events.EventCreatedEvent;
import com.clloret.days.model.events.ShowMessageEvent;
import dagger.android.AndroidInjection;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;

public class EventCreateActivity extends
    BaseMvpActivity<EventCreateView, EventCreatePresenter> implements EventCreateView,
    SelectTagsDialogListener {

  @Inject
  EventCreatePresenter injectPresenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.event_switcher)
  ViewSwitcher eventSwitcher;

  @BindView(R.id.description_switch)
  ViewSwitcher descriptionSwitcher;

  @BindView(R.id.layout_eventdetail_name)
  TextInputLayout nameLayout;

  @BindView(R.id.edittext_eventdetail_name)
  EditText nameEdit;

  @BindView(R.id.edittext_eventdetail_description)
  EditText descriptionEdit;

  @BindView(R.id.textview_eventdetail_date)
  TextView dateText;

  @BindView(R.id.textview_eventdetail_tags)
  TextView tagsText;

  @BindView(R.id.textview_eventdetail_reminder)
  TextView reminderText;

  @BindView(R.id.textview_eventdetail_reset)
  TextView timeLapseResetText;

  @BindView(R.id.fab)
  FloatingActionButton fab;

  @Inject
  SelectTagsHelper selectTagsHelper;

  @Inject
  SelectPeriodHelper selectPeriodHelper;

  @Inject
  PeriodTextFormatter periodTextFormatter;

  private LocalDate selectedDate;
  private EventViewModel event = new EventViewModel();

  public static Intent getCallingIntent(Context context) {

    return new Intent(context, EventCreateActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);

    ButterKnife.bind(this);

    configureActionBar(toolbar);

    fab.setImageDrawable(getDrawable(R.drawable.ic_save_wht_24dp));

    eventSwitcher.showNext();
    descriptionSwitcher.showNext();

    presenter.loadTags();
  }

  @Override
  protected void injectDependencies() {

    AndroidInjection.inject(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_event_details, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {

    menu.findItem(R.id.menu_add_to_calendar).setVisible(false);

    return super.onPrepareOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    switch (id) {

      case R.id.menu_delete:
        discardEvent();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void discardEvent() {

    EventBus.getDefault().post(new ShowMessageEvent(getString(R.string.msg_event_discarded)));

    NavUtils.navigateUpFromSameTask(this);
  }

  private void saveEvent() {

    nameLayout.setErrorEnabled(false);

    String name = nameEdit.getText().toString();
    String description = descriptionEdit.getText().toString();
    Date date = selectedDate != null ? selectedDate.toDate() : null;
    String[] tags = selectTagsHelper.getMapTags().getKeySelection(TagViewModel::getId)
        .toArray(new String[0]);

    event.setName(name);
    event.setDescription(description);
    event.setDate(date);
    event.setTags(tags);

    presenter.createEvent(event);
  }

  @NonNull
  @Override
  public EventCreatePresenter createPresenter() {

    return injectPresenter;
  }

  @Override
  public void onSuccessfully(EventViewModel event) {

    EventBus.getDefault().post(new EventCreatedEvent(event));

    finish();
  }

  @Override
  public void onError(String message) {

    showToastMessage(message);
  }

  @Override
  public void setData(List<TagViewModel> data) {

    selectTagsHelper.setMapTags(data);

    showSelectedTags();
    showSelectedReminder();
    showSelectedTimeLapseReset();
  }

  @Override
  public void showError(Throwable t) {

    showToastMessage(t.getLocalizedMessage());
  }

  @Override
  public void onEmptyEventNameError() {

    nameLayout.setError(getString(R.string.msg_error_event_name_required));
    nameLayout.setErrorEnabled(true);
    nameLayout.getEditText().requestFocus();
  }

  @Override
  public void onEmptyEventDateError() {

    showToastMessage(R.string.msg_error_event_date_required);
  }

  private void showSelectedTags() {

    tagsText.setText(selectTagsHelper.showSelectedTags());
  }

  private void showSelectedReminder() {

    reminderText.setText(periodTextFormatter.formatReminder(event));
  }

  private void showSelectedTimeLapseReset() {

    timeLapseResetText.setText(periodTextFormatter.formatTimeLapseReset(event));
  }

  @OnClick(R.id.layout_eventdetail_date)
  public void onClickDay() {

    selectDate();
  }

  @OnClick(R.id.layout_eventdetail_tags)
  public void onClickTags() {

    selectTags();
  }

  @OnClick(R.id.layout_eventdetail_reminder)
  public void onClickReminder() {

    selectReminder();
  }

  @OnClick(R.id.button_eventdetail_clear_reminder)
  public void onClickClearReminder() {

    clearReminder();
  }

  @OnClick(R.id.layout_eventdetail_reset)
  public void onClickReset() {

    selectTimeLapseReset();
  }

  @OnClick(R.id.fab)
  public void onClickFab() {

    saveEvent();
  }

  private void selectDate() {

    SelectDateHelper.selectDate(this, (date, formattedDate) -> {

      selectedDate = date;
      dateText.setText(formattedDate);
    });
  }

  private void selectTags() {

    selectTagsHelper.showSelectTagsDialog(this, this::showSnackbarMessage);
  }

  private void selectReminder() {

    selectPeriodHelper.showSelectReminderDialog(this, event, (period, timeUnit) -> {
      event.setReminder(period);
      event.setReminderUnit(timeUnit);

      showSelectedReminder();
    });
  }

  private void clearReminder() {

    if (event.hasReminder()) {
      event.setReminder(null);
      event.setReminderUnit(null);

      showSelectedReminder();
    }
  }

  private void selectTimeLapseReset() {

    selectPeriodHelper.showSelectTimeLapseResetDialog(this, event,
        (period, timeUnit) -> {
          event.setTimeLapse(period);
          event.setTimeLapseUnit(timeUnit);

          showSelectedTimeLapseReset();
        });
  }

  @Override
  public void onFinishTagsDialog(Collection<TagViewModel> selectedItems) {

    selectTagsHelper.updateSelectedTags(selectedItems);
    showSelectedTags();
  }
}
