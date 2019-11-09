package com.clloret.days.events.edit;

import static com.clloret.days.utils.FabProgressUtils.fixFinalIconPosition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.domain.utils.DateUtils;
import com.clloret.days.events.common.PeriodTextFormatter;
import com.clloret.days.events.common.SelectDateHelper;
import com.clloret.days.events.common.SelectPeriodHelper;
import com.clloret.days.events.common.SelectTagsDialog.SelectTagsDialogListener;
import com.clloret.days.events.common.SelectTagsHelper;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.events.EventDeletedEvent;
import com.clloret.days.model.events.EventModifiedEvent;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import dagger.android.AndroidInjection;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;

@SuppressWarnings("PMD.TooManyMethods")
public class EventEditActivity
    extends BaseMvpActivity<EventEditView, EventEditPresenter>
    implements EventEditView, SelectTagsDialogListener, FABProgressListener {

  private static final String EXTRA_EVENT = "com.clloret.days.extras.EXTRA_EVENT";

  @Inject
  EventEditPresenter injectPresenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.event_switcher)
  ViewSwitcher eventSwitcher;

  @BindView(R.id.description_switch)
  ViewSwitcher descriptionSwitcher;

  @BindView(R.id.layout_eventdetail_name)
  TextInputLayout nameLayout;

  @BindView(R.id.textview_eventdetail_name)
  TextView nameText;

  @BindView(R.id.edittext_eventdetail_name)
  EditText nameEdit;

  @BindView(R.id.textview_eventdetail_description)
  TextView descriptionText;

  @BindView(R.id.edittext_eventdetail_description)
  EditText descriptionEdit;

  @BindView(R.id.layout_eventdetail_date)
  View dateLayout;

  @BindView(R.id.layout_eventdetail_tags)
  View tagsLayout;

  @BindView(R.id.layout_eventdetail_reminder)
  View reminderLayout;

  @BindView(R.id.button_eventdetail_clear_reminder)
  View clearReminderButton;

  @BindView(R.id.layout_eventdetail_reset)
  View resetLayout;

  @BindView(R.id.textview_eventdetail_date)
  TextView dateText;

  @BindView(R.id.textview_eventdetail_tags)
  TextView tagsText;

  @BindView(R.id.textview_eventdetail_reminder)
  TextView reminderText;

  @BindView(R.id.textview_eventdetail_reset)
  TextView timeLapseResetText;

  @BindView(R.id.fab)
  FloatingActionButton fabEditSave;

  @BindView(R.id.fabProgress)
  FABProgressCircle fabProgress;

  @Inject
  SelectTagsHelper selectTagsHelper;

  @Inject
  SelectPeriodHelper selectPeriodHelper;

  @Inject
  PeriodTextFormatter periodTextFormatter;

  private EventViewModel originalEvent;
  private EventViewModel modifiedEvent;
  private LocalDate selectedDate;
  private boolean editing;

  public static Intent getCallingIntent(Context context, EventViewModel event) {

    Intent intent = new Intent(context, EventEditActivity.class);
    intent.putExtra(EXTRA_EVENT, event);

    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);

    ButterKnife.bind(this);

    configureActionBar(toolbar);

    setControlsClickable(false);

    fixFinalIconPosition(fabProgress);
    fabProgress.attachListener(this);

    originalEvent = Objects.requireNonNull(getIntent().getParcelableExtra(EXTRA_EVENT));
    modifiedEvent = originalEvent.clone();

    dateText.setInputType(InputType.TYPE_NULL);
    dateText.setKeyListener(null);
    dateText.setOnFocusChangeListener((v, hasFocus) -> {

      if (hasFocus) {
        selectDate();
      }
    });

    showData();

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
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    switch (id) {

      case R.id.menu_add_to_calendar:
        addEventToCalendar();
        return true;

      case R.id.menu_delete:
        deleteEvent();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @NonNull
  @Override
  public EventEditPresenter createPresenter() {

    return injectPresenter;
  }

  @Override
  public void onSuccessfully(EventViewModel event) {

    EventBus.getDefault().post(new EventModifiedEvent(event));
  }

  @Override
  public void onError(String message) {

    showToastMessage(message);
  }

  @Override
  public void setData(List<TagViewModel> data) {

    selectTagsHelper.setMapTags(data);

    if (modifiedEvent.getTags().length != 0) {
      selectTagsHelper.selectTagsFromEvent(modifiedEvent);
    }

    showSelectedTags();
    showSelectedReminder();
    showSelectedTimeLapseReset();
  }

  @Override
  public void showError(Throwable t) {

    showToastMessage(t.getLocalizedMessage());
  }

  @Override
  public void showIndeterminateProgress() {

    fabProgress.show();
  }

  @Override
  public void showIndeterminateProgressFinalAnimation() {

    fabProgress.beginFinalAnimation();

  }

  @Override
  public void hideIndeterminateProgress() {

    fabProgress.hide();
  }

  @Override
  public void onEmptyEventNameError() {

    nameLayout.setError(getString(R.string.msg_error_event_name_required));
    nameLayout.setErrorEnabled(true);
    nameLayout.getEditText().requestFocus();
  }

  @Override
  public void deleteSuccessfully(EventViewModel event, boolean deleted) {

    if (deleted) {
      EventBus.getDefault().post(new EventDeletedEvent(event));
      finish();
    } else {
      showSnackbarMessage(R.string.msg_error_event_could_not_be_deleted);
    }
  }

  @Override
  public void onFinishTagsDialog(Collection<TagViewModel> selectedItems) {

    selectTagsHelper.updateSelectedTags(selectedItems);
    showSelectedTags();
  }

  @Override
  public void onFABProgressAnimationEnd() {

    finish();
  }

  @OnClick(R.id.layout_eventdetail_date)
  public void onClickDay() {

    selectDate();
  }

  @OnClick(R.id.layout_eventdetail_tags)
  public void onClickTag() {

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

    if (editing) {

      saveEvent();
    } else {

      editMode();
    }
  }

  private void setControlsClickable(boolean clickable) {

    setControlClickable(clickable, dateLayout);
    setControlClickable(clickable, tagsLayout);
    setControlClickable(clickable, reminderLayout);
    setControlClickable(clickable, resetLayout);
    setControlClickable(clickable, clearReminderButton);
  }

  private void setControlClickable(boolean clickable, View view) {

    view.setClickable(clickable);
    view.setFocusable(clickable);
  }

  private void addEventToCalendar() {

    Date eventDate = modifiedEvent.getDate();

    if (eventDate == null) {
      return;
    }

    Intent calIntent = new Intent(Intent.ACTION_INSERT);
    calIntent.setType("vnd.android.cursor.item/event");
    calIntent.putExtra(Events.TITLE, modifiedEvent.getName());
    calIntent.putExtra(Events.DESCRIPTION, modifiedEvent.getDescription());
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventDate.getTime());
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventDate.getTime());

    startActivity(calIntent);
  }

  private void deleteEvent() {

    presenter.deleteEvent(modifiedEvent);
  }

  private void showData() {

    String name = modifiedEvent.getName();
    this.nameText.setText(name);
    nameEdit.setText(name);
    nameEdit.selectAll();

    String description = modifiedEvent.getDescription();
    this.descriptionText.setText(description);
    descriptionEdit.setText(description);
    descriptionEdit.selectAll();

    selectedDate = new LocalDate(modifiedEvent.getDate());

    dateText.setText(DateUtils.formatDate(modifiedEvent.getDate()));
  }

  private void showSelectedTags() {

    tagsText.setText(selectTagsHelper.showSelectedTags());
  }

  private void showSelectedReminder() {

    reminderText.setText(periodTextFormatter.formatReminder(modifiedEvent));
  }

  private void showSelectedTimeLapseReset() {

    timeLapseResetText.setText(periodTextFormatter.formatTimeLapseReset(modifiedEvent));
  }

  private void selectDate() {

    SelectDateHelper.selectDate(this, selectedDate, (date, formattedDate) -> {

      selectedDate = date;
      dateText.setText(formattedDate);
    });

  }

  private void editMode() {

    editing = true;

    fabEditSave.setImageDrawable(getDrawable(R.drawable.ic_save_wht_24dp));

    eventSwitcher.showNext();
    descriptionSwitcher.showNext();

    setControlsClickable(true);
  }

  private void selectTags() {

    selectTagsHelper.showSelectTagsDialog(this, this::showSnackbarMessage);
  }

  private void selectReminder() {

    selectPeriodHelper.showSelectReminderDialog(this, modifiedEvent, (period, timeUnit) -> {
      modifiedEvent.setReminder(period);
      modifiedEvent.setReminderUnit(timeUnit);

      showSelectedReminder();
    });
  }

  private void clearReminder() {

    if (modifiedEvent.hasReminder()) {
      modifiedEvent.setReminder(null);

      showSelectedReminder();
    }
  }

  private void selectTimeLapseReset() {

    selectPeriodHelper.showSelectTimeLapseResetDialog(this, modifiedEvent,
        (period, timeUnit) -> {
          modifiedEvent.setTimeLapse(period);
          modifiedEvent.setTimeLapseUnit(timeUnit);

          showSelectedTimeLapseReset();
        });
  }

  private void saveEvent() {

    nameLayout.setErrorEnabled(false);

    String name = nameEdit.getText().toString();
    String description = descriptionEdit.getText().toString();
    Date date = selectedDate.toDate();
    String[] tags = selectTagsHelper.getMapTags().getKeySelection(TagViewModel::getId)
        .toArray(new String[0]);

    modifiedEvent.setName(name);
    modifiedEvent.setDescription(description);
    modifiedEvent.setDate(date);
    modifiedEvent.setTags(tags);

    presenter.saveEvent(modifiedEvent, originalEvent);
  }

}
