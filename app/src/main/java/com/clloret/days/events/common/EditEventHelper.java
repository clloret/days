package com.clloret.days.events.common;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.clloret.days.domain.entities.Event.TimeUnit;
import com.clloret.days.domain.events.EventPeriodFormat;
import com.clloret.days.domain.utils.DateUtils;
import com.clloret.days.domain.utils.Optional;
import com.clloret.days.domain.utils.TimeProvider;
import com.clloret.days.events.common.SelectTagsDialog.SelectTagsDialogListener;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.TagViewModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

public class EditEventHelper implements SelectTagsDialogListener {

  private final SelectTagsHelper selectTagsHelper;
  private final SelectPeriodHelper selectPeriodHelper;
  private final PeriodTextFormatter periodTextFormatter;
  private final EventPeriodFormat eventPeriodFormat;
  private final TimeProvider timeProvider;
  private CommonEventView view;
  private EventViewModel originalEvent;
  private EventViewModel modifiedEvent;
  private LocalDate selectedDate;
  private Optional<TagViewModel> selectedTag = Optional.empty();

  @Inject
  public EditEventHelper(
      SelectTagsHelper selectTagsHelper,
      SelectPeriodHelper selectPeriodHelper,
      PeriodTextFormatter periodTextFormatter,
      EventPeriodFormat eventPeriodFormat,
      TimeProvider timeProvider) {

    this.selectTagsHelper = selectTagsHelper;
    this.selectPeriodHelper = selectPeriodHelper;
    this.periodTextFormatter = periodTextFormatter;
    this.eventPeriodFormat = eventPeriodFormat;
    this.timeProvider = timeProvider;

    this.selectTagsHelper.setDialogListener(this);
  }

  private void formatAndShowDate() {

    String formattedDate = DateUtils.formatDate(modifiedEvent.getDate());
    view.showDate(formattedDate);
  }

  private void formatAndShowPeriodText() {

    Date currentDate = timeProvider.getCurrentDate().toDate();
    String periodFormatted = eventPeriodFormat
        .getTimeLapseFormatted(selectedDate.toDate(), currentDate, PeriodType.yearMonthDay());
    view.showPeriodText(periodFormatted);
  }

  private void formatAndShowReminderText() {

    String formatReminder = periodTextFormatter.formatReminder(modifiedEvent);
    view.showSelectedReminder(formatReminder);
  }

  private void formatAndShowTimeLapseReset() {

    String formatTimeLapseReset = periodTextFormatter.formatTimeLapseReset(modifiedEvent);
    view.showSelectedTimeLapseReset(formatTimeLapseReset);
  }

  private void formatAndShowSelectedTags() {

    String selectedTags = selectTagsHelper.showSelectedTags();
    view.showSelectedTags(selectedTags);
  }

  public void selectDate(Context context) {

    LocalDate today = timeProvider.getCurrentDate();
    LocalDate defaultDate = selectedDate == null ? today : selectedDate;
    SelectDateHelper.selectDate(context, defaultDate, (date, formattedDate) -> {

      selectedDate = date;
      view.showDate(formattedDate);

      formatAndShowPeriodText();
    });
  }

  public void selectTags(FragmentManager fragmentManager) {

    selectTagsHelper.showSelectTagsDialog(fragmentManager, message -> view.showError(message));
  }

  public void selectReminder(FragmentManager fragmentManager) {

    selectPeriodHelper
        .showSelectReminderDialog(fragmentManager, modifiedEvent, (period, timeUnit) -> {
          modifiedEvent.setReminder(period);
          modifiedEvent.setReminderUnit(timeUnit);

          formatAndShowReminderText();
        });
  }

  public void clearReminder() {

    if (modifiedEvent.hasReminder()) {
      modifiedEvent.setReminder(null);

      formatAndShowReminderText();
    }
  }

  public void clearTimeLapseReset() {

    if (modifiedEvent.hasTimeLapseReset()) {
      modifiedEvent.setTimeLapse(0);
      modifiedEvent.setTimeLapseUnit(TimeUnit.DAY);

      formatAndShowTimeLapseReset();
    }
  }

  public void selectTimeLapseReset(FragmentManager fragmentManager) {

    selectPeriodHelper.showSelectTimeLapseResetDialog(fragmentManager, modifiedEvent,
        (period, timeUnit) -> {
          modifiedEvent.setTimeLapse(period);
          modifiedEvent.setTimeLapseUnit(timeUnit);

          formatAndShowTimeLapseReset();
        });
  }

  public EventViewModel getOriginalEvent() {

    return originalEvent;
  }

  public void setOriginalEvent(EventViewModel originalEvent) {

    this.originalEvent = originalEvent;
    this.modifiedEvent = originalEvent.clone();
    Date eventDate = modifiedEvent.getDate();
    if (eventDate != null) {
      this.selectedDate = new LocalDate(eventDate);
    }
  }

  public EventViewModel getModifiedEvent() {

    return modifiedEvent;
  }

  public LocalDate getSelectedDate() {

    return selectedDate;
  }

  public void setAvailableTags(List<TagViewModel> data) {

    selectTagsHelper.setMapTags(data);

    if (modifiedEvent.getTags().length != 0) {
      selectTagsHelper.selectTagsFromEvent(modifiedEvent);
    }

    selectedTag.ifPresent(selectTagsHelper::addTagToSelection);

    formatAndShowSelectedTags();
  }

  public String[] getSelectedTags() {

    return selectTagsHelper.getMapTags().getKeySelection(TagViewModel::getId)
        .toArray(new String[0]);
  }

  public void setView(CommonEventView view) {

    this.view = view;
  }

  public void showData() {

    formatAndShowDate();
    formatAndShowPeriodText();
    formatAndShowReminderText();
    formatAndShowTimeLapseReset();
  }

  public void setSelectedTag(@Nullable TagViewModel selectedTag) {

    this.selectedTag = Optional.ofNullable(selectedTag);
  }

  @Override
  public void onFinishTagsDialog(Collection<TagViewModel> selectedItems) {

    selectTagsHelper.updateSelectedTags(selectedItems);
    formatAndShowSelectedTags();
  }

}
