package com.clloret.days.screenshots;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;
import com.clloret.days.NotificationsCommon;
import com.clloret.days.R;
import com.clloret.days.TestApp;
import com.clloret.days.activities.MainActivity;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.EventPeriodFormat;
import com.clloret.days.domain.utils.StringResourceProvider;
import java.util.Locale;
import java.util.Objects;
import javax.inject.Inject;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.cleanstatusbar.CleanStatusBar;

@LargeTest
@RunWith(AndroidJUnit4.class)
public abstract class BaseScreenshotsTest {

  static final Locale DEVICE_LOCALE = new Locale("en", "US");

  @Rule
  public final ActivityScenarioRule<MainActivity> activityRule =
      new ActivityScenarioRule<>(MainActivity.class);

  @Rule
  public final GrantPermissionRule grantPermissionRule = GrantPermissionRule
      .grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

  @Inject
  StringResourceProvider stringResourceProvider;

  @Inject
  EventPeriodFormat eventPeriodFormat;

  private Resources resources;
  private String expectedAppName;
  private UiDevice uiDevice;
  private NotificationsCommon notificationsCommon;

  @SuppressWarnings("SameParameterValue")
  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {

        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {

        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }

  private static String getFunctionName(Object object) {

    return Objects.requireNonNull(object.getClass().getEnclosingMethod()).getName();
  }

  private static void configureDemoMode() {

    new CleanStatusBar()
        .setClock("1200")
        .setNumberOfSims(1)
        .setShowNotifications(false)
        .enable();
  }

  @BeforeClass
  public static void beforeAll() {

    configureDemoMode();
  }

  @AfterClass
  public static void afterAll() {

    CleanStatusBar.disable();
  }

  private void openNavigationDrawer() {

    onView(withId(R.id.drawer_layout))
        .check(matches(isClosed(Gravity.LEFT)))
        .perform(DrawerActions.open());
  }

  private UiDevice getUiDevice() {

    return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
  }

  @Before
  public void setUp() {

    TestApp app = (TestApp) getInstrumentation()
        .getTargetContext().getApplicationContext();

    resources = app.getResources();
    uiDevice = getUiDevice();

    app.getAppComponent().inject(this);

    ActivityScenario<MainActivity> scenario = activityRule.getScenario();
    scenario.onActivity(activity -> {
      Resources res = activity.getResources();
      expectedAppName = res.getString(R.string.app_name);
    });

    notificationsCommon = new NotificationsCommon(stringResourceProvider, eventPeriodFormat);

    configureDemoMode();
  }

  @Test
  public void makeScreenshot_NewEvent() {

    onView(withId(R.id.fab_main_newevent))
        .perform(click());

    onView(withId(R.id.edittext_eventdetail_name))
        .check(matches(isDisplayed()));

    Espresso.closeSoftKeyboard();

    Screengrab.screenshot(getFunctionName(new Object() {
    }));
  }

  @Test
  public void makeScreenshot_MainView() throws InterruptedException {

    Thread.sleep(2000L);

    onView(withId(R.id.fab_main_newevent))
        .check(matches(isDisplayed()));

    Screengrab.screenshot(getFunctionName(new Object() {
    }));
  }

  @Test
  public void makeScreenshot_ShowEventDetails() {

    onView(withId(R.id.recyclerView))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    onView(withId(R.id.textview_eventdetail_name))
        .check(matches(isDisplayed()));

    Screengrab.screenshot(getFunctionName(new Object() {
    }));
  }

  @Test
  public void makeScreenshot_ShowMenu() {

    openNavigationDrawer();

    Screengrab.screenshot(getFunctionName(new Object() {
    }));
  }

  @Test
  public void makeScreenshot_ShowEventOrder() {

    Context context = getInstrumentation().getTargetContext();
    openActionBarOverflowOrOptionsMenu(context);
    onView(withText(R.string.title_order))
        .perform(click());

    Screengrab.screenshot(getFunctionName(new Object() {
    }));
  }

  @Test
  public void makeScreenshot_NewTag() {

    openNavigationDrawer();

    DataInteraction item = onData(
        Matchers.hasToString(resources.getString(R.string.action_new_tag)))
        .inAdapterView(allOf(withId(R.id.listView),
            childAtPosition(
                withId(R.id.navigation_drawer),
                0)));
    item.perform(click());

    Espresso.closeSoftKeyboard();

    Screengrab.screenshot(getFunctionName(new Object() {
    }));
  }

  @Test
  public void makeScreenshot_ShowSettings() {

    openNavigationDrawer();

    DataInteraction item = onData(
        Matchers.hasToString(resources.getString(R.string.action_settings)))
        .inAdapterView(allOf(withId(R.id.listView),
            childAtPosition(
                withId(R.id.navigation_drawer),
                0)));

    item.perform(click());

    Screengrab.screenshot(getFunctionName(new Object() {
    }));
  }

  @Test
  public void makeScreenshot_ShowNotification() {

    uiDevice.pressHome();

    final Event event = notificationsCommon.getSampleEventWithTodayDate(0);
    notificationsCommon.showNotification(event);

    uiDevice.openNotification();
    uiDevice.wait(Until.hasObject(By.textStartsWith(expectedAppName)), NotificationsCommon.TIMEOUT);

    Screengrab.screenshot(getFunctionName(new Object() {
    }));

    uiDevice.pressBack();
  }

}
