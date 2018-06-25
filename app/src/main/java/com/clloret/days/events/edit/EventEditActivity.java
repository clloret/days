package com.clloret.days.events.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.events.common.SelectDateHelper;
import com.clloret.days.events.common.SelectTagsDialog.SelectTagsDialogListener;
import com.clloret.days.events.common.SelectTagsHelper;
import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.Tag;
import com.clloret.days.model.events.EventDeletedEvent;
import com.clloret.days.model.events.EventModifiedEvent;
import com.clloret.days.utils.DateUtils;
import dagger.android.AndroidInjection;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;

public class EventEditActivity
    extends BaseMvpActivity<EventEditView, EventEditPresenter>
    implements EventEditView, SelectTagsDialogListener {

  private static final String EXTRA_EVENT = "event";

  @Inject
  EventEditPresenter injectPresenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.event_switcher)
  ViewSwitcher eventSwicher;

  @BindView(R.id.description_switch)
  ViewSwitcher descriptionSwicher;

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
  View layDate;

  @BindView(R.id.layout_eventdetail_tags)
  View layTags;

  @BindView(R.id.textview_eventdetail_date)
  TextView dateText;

  @BindView(R.id.textview_eventdetail_tags)
  TextView tagsText;

  @BindView(R.id.fab)
  FloatingActionButton fab;

  private Event event;
  private LocalDate selectedDate;
  private SelectTagsHelper selectTagsHelper = new SelectTagsHelper();
  private boolean editing;

  public static Intent getCallingIntent(Context context, Event event) {

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

    presenter.loadTags();

    dateText.setInputType(InputType.TYPE_NULL);
    dateText.setKeyListener(null);
    dateText.setOnFocusChangeListener((v, hasFocus) -> {

      if (hasFocus) {
        selectDate();
      }
    });

    event = getIntent().getParcelableExtra(EXTRA_EVENT);

    showData();
  }

  @Override
  protected void injectDependencies() {

    AndroidInjection.inject(this);
  }

  private void setControlsClickable(boolean clickable) {

    layDate.setClickable(clickable);
    layDate.setFocusable(clickable);

    layTags.setClickable(clickable);
    layTags.setFocusable(clickable);
  }

  private void showData() {

    String name = event.getName();
    this.nameText.setText(name);
    nameEdit.setText(name);
    nameEdit.selectAll();

    String description = event.getDescription();
    this.descriptionText.setText(description);
    descriptionEdit.setText(description);
    descriptionEdit.selectAll();

    selectedDate = new LocalDate(event.getDate());

    dateText.setText(DateUtils.formatDate(event.getDate()));
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

  private void addEventToCalendar() {

    Intent calIntent = new Intent(Intent.ACTION_INSERT);
    calIntent.setType("vnd.android.cursor.item/event");
    calIntent.putExtra(Events.TITLE, event.getName());
    calIntent.putExtra(Events.DESCRIPTION, event.getDescription());

    calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getDate().getTime());
    calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getDate().getTime());

    startActivity(calIntent);
  }

  private void deleteEvent() {

    presenter.deleteEvent(event);
  }

  @NonNull
  @Override
  public EventEditPresenter createPresenter() {

    return injectPresenter;
  }

  @Override
  public void onSuccessfully(Event event) {

    EventBus.getDefault().post(new EventModifiedEvent(event));
    finish();
  }

  @Override
  public void onError(String message) {

    showToastMessage(message);
  }

  @Override
  public void setData(List<Tag> data) {

    selectTagsHelper.setMapTags(data);

    if (event.getTags().length == 0) {
      return;
    }

    selectTagsHelper.selectTagsFromEvent(event);

    showSelectedTags();
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
  public void deleteSuccessfully(Event event, boolean deleted) {

    if (deleted) {
      EventBus.getDefault().post(new EventDeletedEvent(event));
      finish();
    } else {
      showSnackbarMessage(R.string.msg_error_event_could_not_be_deleted);
    }
  }

  private void showSelectedTags() {

    tagsText.setText(selectTagsHelper.showSelectedTags());
  }

  @Override
  public void onFinishDialog(Collection<Tag> selectedItems) {

    selectTagsHelper.updateSelectedTags(selectedItems);
    showSelectedTags();
  }

  private void selectDate() {

    SelectDateHelper.selectDate(this, (date, formatedDate) -> {

      selectedDate = date;
      dateText.setText(formatedDate);
    });

  }

  @OnClick(R.id.layout_eventdetail_date)
  public void onClickDay() {

    selectDate();
  }

  @OnClick(R.id.layout_eventdetail_tags)
  public void onClickTag() {

    selectTags();
  }

  @OnClick(R.id.fab)
  public void onClickFab() {

    if (editing) {

      saveEvent();
    } else {

      editMode();
    }
  }

  private void editMode() {

    editing = true;

    fab.setImageDrawable(getDrawable(R.drawable.ic_save_wht_24dp));

    eventSwicher.showNext();
    descriptionSwicher.showNext();

    setControlsClickable(true);
  }

  private void selectTags() {

    selectTagsHelper.showSelectTagsDialog(this, this::showSnackbarMessage);
  }

  private void saveEvent() {

    nameLayout.setErrorEnabled(false);

    String name = nameEdit.getText().toString();
    String description = descriptionEdit.getText().toString();
    Date date = selectedDate.toDate();
    String[] tags = selectTagsHelper.getMapTags().getKeySelection(Tag::getId)
        .toArray(new String[0]);

    event.setName(name);
    event.setDescription(description);
    event.setDate(date);
    event.setTags(tags);

    presenter.saveEvent(event);
  }
}
