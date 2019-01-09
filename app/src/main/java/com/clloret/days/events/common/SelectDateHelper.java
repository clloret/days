package com.clloret.days.events.common;

import android.app.DatePickerDialog;
import android.content.Context;
import com.clloret.days.utils.DateUtils;
import org.joda.time.LocalDate;

public class SelectDateHelper {

  public static void selectDate(Context context, LocalDate initialDate,
      SelectDateHelperListener listener) {

    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
        (datePicker, year, monthOfYear, dayOfMonth) -> {

          LocalDate selectedDate = initialDate.withYear(year).withMonthOfYear(monthOfYear + 1)
              .withDayOfMonth(dayOfMonth);

          listener.onFinishDialog(selectedDate, DateUtils.formatDate(selectedDate));

        }, initialDate.getYear(), initialDate.getMonthOfYear() - 1, initialDate.getDayOfMonth());

    datePickerDialog.show();
  }

  public interface SelectDateHelperListener {

    void onFinishDialog(LocalDate date, String formattedDate);
  }
}
