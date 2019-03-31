package com.clloret.days.fakes;

import com.clloret.days.domain.utils.TimeProvider;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class TestTimeProvider implements TimeProvider {

  @Override
  public LocalDate getCurrentDate() {

    return new LocalDate(2019, 3, 25);
  }

  @Override
  public DateTime getCurrentTime() {

    return new LocalTime(20, 0).toDateTimeToday();
  }
}
