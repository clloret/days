package com.clloret.days;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.content.res.Resources;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;
import com.clloret.days.activities.MainActivity;
import com.clloret.days.dagger.AppTestComponent;
import com.clloret.days.dagger.modules.TestDataModule;
import com.clloret.days.device.reminders.ReminderManagerImpl;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.ResetEventDateUseCase;
import com.clloret.days.domain.utils.Optional;
import com.clloret.days.domain.utils.StringResourceProvider;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.utils.NotificationsIntentsImpl;
import java.util.Date;
import javax.inject.Inject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class NotificationsTest {

  private static final long TIMEOUT = 5_000L;
  private static final String CLEAR_ALL_NOTIFICATION_RES = "com.android.systemui:id/dismiss_text";
  private static final String EXPECTED_ACTION_RES = "android:id/action0";

  @Rule
  public ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);

  @Inject
  DeleteEventUseCase deleteEventUseCase;

  @Inject
  ResetEventDateUseCase resetEventDateUseCase;

  @Inject
  StringResourceProvider stringResourceProvider;

  private String expectedActionNameDelete;
  private String expectedActionNameReset;
  private String expectedAppName;
  private UiDevice uiDevice;

  @Before
  public void setUp() {

    TestApp app = (TestApp) InstrumentationRegistry.getInstrumentation()
        .getTargetContext().getApplicationContext();

    AppTestComponent appComponent = app.getAppComponent();
    appComponent.inject(this);

    Resources res = activityRule.getActivity().getResources();
    expectedAppName = res.getString(R.string.app_name);
    expectedActionNameDelete = stringResourceProvider.getEventDeleteNotificationAction();
    expectedActionNameReset = stringResourceProvider.getEventResetNotificationAction();

    uiDevice = getUiDevice();
  }

  @After
  public void tearDown() {

    hideNotification();
  }

  private UiDevice getUiDevice() {

    return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
  }

  private void showNotification(Event event) {

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    NotificationsIntentsImpl notificationsIntents = new NotificationsIntentsImpl(context,
        new EventViewModelMapper());

    ReminderManagerImpl reminderManager = new ReminderManagerImpl(context, notificationsIntents,
        stringResourceProvider);

    String contentTitle = event.getName();
    String contentText = "6 days ago";
    String bigText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent et tincidunt odio.";
    reminderManager
        .addReminder(event, event.getId(), event.getDate(), contentTitle, contentText,
            Optional.of(bigText));
  }

  @SuppressWarnings("unused")
  private void clearAllNotifications() {

    UiDevice uiDevice = getUiDevice();
    uiDevice.openNotification();
    uiDevice.wait(Until.hasObject(By.textStartsWith(expectedAppName)), TIMEOUT);
    UiObject2 clearAll = uiDevice.findObject(By.res(CLEAR_ALL_NOTIFICATION_RES));
    clearAll.click();
  }

  @SuppressWarnings("unused")
  private void hideNotification() {

    uiDevice.swipe(
        200,
        200,
        200,
        100,
        5
    );
  }

  private Event createTestEvent(String id, String name) {

    Event event = new Event();
    event.setId(id);
    event.setName(name);
    event.setDate(new Date());

    return event;
  }

  @Test
  public void showNotification_WhenOpen_ShowEventDetails() {

    uiDevice.pressHome();

    showNotification(
        createTestEvent(TestDataModule.TEST_EVENT_1_ID, TestDataModule.TEST_EVENT_1_NAME));

    uiDevice.openNotification();
    uiDevice.wait(Until.hasObject(By.textStartsWith(expectedAppName)), TIMEOUT);
    UiObject2 title = uiDevice.findObject(By.text(TestDataModule.TEST_EVENT_1_NAME));
    title.click();
    uiDevice.wait(Until.hasObject(By.text(TestDataModule.TEST_EVENT_1_NAME)), TIMEOUT);

    onView(withId(R.id.textview_eventdetail_name))
        .check(matches(isDisplayed()))
        .check(matches(withText(TestDataModule.TEST_EVENT_1_NAME)));

    uiDevice.pressBack();
  }

  @Test
  public void showNotification_WhenActionDelete_DeleteEvent() throws UiObjectNotFoundException {

    uiDevice.pressHome();

    showNotification(
        createTestEvent(TestDataModule.TEST_EVENT_2_ID, TestDataModule.TEST_EVENT_2_NAME));

    uiDevice.openNotification();

    uiDevice.wait(Until.hasObject(By.textStartsWith(expectedAppName)), TIMEOUT);
    UiObject notificationOpenItem = uiDevice.findObject(new UiSelector()
        .resourceId(EXPECTED_ACTION_RES)
        .description(expectedActionNameDelete)
        .enabled(true));
    notificationOpenItem.click();

    uiDevice.pressHome();

    verify(deleteEventUseCase, timeout(TIMEOUT).atLeastOnce()).execute(any());
  }

  @Test
  public void showNotification_WhenActionReset_ResetEventDate() throws UiObjectNotFoundException {

    uiDevice.pressHome();

    showNotification(
        createTestEvent(TestDataModule.TEST_EVENT_3_ID, TestDataModule.TEST_EVENT_3_NAME));

    uiDevice.openNotification();

    uiDevice.wait(Until.hasObject(By.textStartsWith(expectedAppName)), TIMEOUT);
    UiObject notificationOpenItem = uiDevice.findObject(new UiSelector()
        .resourceId(EXPECTED_ACTION_RES)
        .description(expectedActionNameReset)
        .enabled(true));
    notificationOpenItem.click();

    uiDevice.pressHome();

    verify(resetEventDateUseCase, timeout(TIMEOUT).atLeastOnce()).execute(any());
  }

}
