package com.clloret.days.events.common;

import android.app.DatePickerDialog;
import android.content.Context;
import com.clloret.days.utils.DateUtils;
import org.joda.time.LocalDate;

public class SelectDateHelper {

  public static void selectDate(Context context, SelectDateHelperListener listener) {

    final LocalDate ld = new LocalDate();

    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
        (datePicker, year, monthOfYear, dayOfMonth) -> {

          LocalDate selectedDate = ld.withYear(year).withMonthOfYear(monthOfYear + 1)
              .withDayOfMonth(dayOfMonth);

          listener.onFinishDialog(selectedDate, DateUtils.formatDate(selectedDate));

        }, ld.getYear(), ld.getMonthOfYear() - 1, ld.getDayOfMonth());

    datePickerDialog.show();
  }

  public interface SelectDateHelperListener {

    void onFinishDialog(LocalDate date, String formatedDate);
  }
}
