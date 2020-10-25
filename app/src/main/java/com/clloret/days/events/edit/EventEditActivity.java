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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.events.common.CommonEventView;
import com.clloret.days.events.common.EditEventHelper;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.events.EventDeletedEvent;
import com.clloret.days.model.events.EventModifiedEvent;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.github.jorgecastilloprz.listeners.FABProgressListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import dagger.android.AndroidInjection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;

@SuppressWarnings("PMD.TooManyMethods")
public class EventEditActivity
    extends BaseMvpActivity<EventEditView, EventEditPresenter>
    implements EventEditView, CommonEventView, FABProgressListener {

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

  @BindView(R.id.textview_eventdetail_period_text)
  TextView periodText;

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

  @BindView(R.id.favorite_button)
  ImageView favoriteButton;

  @BindView(R.id.fab)
  FloatingActionButton fabEditSave;

  @BindView(R.id.fabProgress)
  FABProgressCircle fabProgress;

  @Inject
  EditEventHelper editEventHelper;

  private boolean editing;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);

    ButterKnife.bind(this);

    configureActionBar(toolbar);

    setControlsClickable(false);

    configureFabProgress();

    dateText.setInputType(InputType.TYPE_NULL);
    dateText.setKeyListener(null);
    dateText.setOnFocusChangeListener((v, hasFocus) -> {

      if (hasFocus) {
        selectDate();
      }
    });

    EventViewModel originalEvent = Objects
        .requireNonNull(getIntent().getParcelableExtra(EXTRA_EVENT));
    editEventHelper.setOriginalEvent(originalEvent);
    editEventHelper.setView(this);
    editEventHelper.setFragmentManager(getSupportFragmentManager());

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

    editEventHelper.setAvailableTags(data);
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
  public void onFABProgressAnimationEnd() {

    finish();
  }

  @Override
  public void showPeriodText(String text) {

    periodText.setText(text);
  }

  @Override
  public void showDate(String text) {

    dateText.setText(text);
  }

  @Override
  public void showSelectedTags(String text) {

    tagsText.setText(text);
  }

  @Override
  public void showSelectedReminder(String text) {

    reminderText.setText(text);
  }

  @Override
  public void showSelectedTimeLapseReset(String text) {

    timeLapseResetText.setText(text);
  }

  @Override
  public void showError(String text) {

    this.showSnackbarMessage(text);
  }

  public static Intent getCallingIntent(Context context, EventViewModel event) {

    Intent intent = new Intent(context, EventEditActivity.class);
    intent.putExtra(EXTRA_EVENT, event);

    return intent;
  }

  private void configureFabProgress() {

    fixFinalIconPosition(fabProgress);
    fabProgress.attachListener(this);
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

  @OnClick(R.id.button_eventdetail_clear_reset)
  public void onClickClearReset() {

    clearTimeLapseReset();
  }

  @OnClick(R.id.favorite_button)
  public void onClickFavorite() {

    editEventHelper.setFavorite(favoriteButton);
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
    setControlClickable(clickable, favoriteButton);
  }

  private void setControlClickable(boolean clickable, View view) {

    view.setClickable(clickable);
    view.setFocusable(clickable);
  }

  private void addEventToCalendar() {

    EventViewModel modifiedEvent = editEventHelper.getModifiedEvent();
    Date eventDate = modifiedEvent.getDate();

    Intent calIntent = new Intent(Intent.ACTION_INSERT);
    calIntent.setType("vnd.android.cursor.item/event");
    calIntent.putExtra(Events.TITLE, modifiedEvent.getName());
    calIntent.putExtra(Events.DESCRIPTION, modifiedEvent.getDescription());
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventDate.getTime());
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventDate.getTime());

    startActivity(calIntent);
  }

  private void editMode() {

    editing = true;

    fabEditSave.setImageDrawable(getDrawable(R.drawable.ic_save_wht_24dp));

    eventSwitcher.showNext();
    descriptionSwitcher.showNext();

    setControlsClickable(true);
  }

  private void showData() {

    EventViewModel modifiedEvent = editEventHelper.getModifiedEvent();

    String name = modifiedEvent.getName();
    this.nameText.setText(name);
    nameEdit.setText(name);
    nameEdit.selectAll();

    String description = modifiedEvent.getDescription();
    this.descriptionText.setText(description);
    descriptionEdit.setText(description);
    descriptionEdit.selectAll();

    editEventHelper.showFavoriteButtonState(favoriteButton);
    editEventHelper.showData();
  }

  private void selectDate() {

    editEventHelper.selectDate(this);
  }

  private void selectTags() {

    editEventHelper.selectTags();
  }

  private void selectReminder() {

    editEventHelper.selectReminder();
  }

  private void clearReminder() {

    editEventHelper.clearReminder();
  }

  private void selectTimeLapseReset() {

    editEventHelper.selectTimeLapseReset();
  }

  private void clearTimeLapseReset() {

    editEventHelper.clearTimeLapseReset();
  }

  private void saveEvent() {

    nameLayout.setErrorEnabled(false);

    String name = nameEdit.getText().toString();
    String description = descriptionEdit.getText().toString();
    String[] selectedTags = editEventHelper.getSelectedTags();

    LocalDate selectedDate = editEventHelper.getSelectedDate();
    Date date = selectedDate.toDate();

    EventViewModel event = editEventHelper.getModifiedEvent();
    event.setName(name);
    event.setDescription(description);
    event.setDate(date);
    event.setTags(selectedTags);

    EventViewModel originalEvent = editEventHelper.getOriginalEvent();

    presenter.saveEvent(event, originalEvent);
  }

  private void deleteEvent() {

    EventViewModel modifiedEvent = editEventHelper.getModifiedEvent();
    presenter.deleteEvent(modifiedEvent);
  }

}
