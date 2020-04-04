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
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import com.clloret.days.R;
import com.clloret.days.TestApp;
import com.clloret.days.activities.MainActivity;
import com.clloret.days.screenshots.demo.DemoMode;
import com.clloret.days.screenshots.screenshot.ScreenshotTakingRule;
import java.util.Locale;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public abstract class BaseScreenshotsTest {

  static final Locale DEVICE_LOCALE = new Locale("en", "US");
  private final ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);
  private final GrantPermissionRule grantPermissionRule = GrantPermissionRule
      .grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
  private final ScreenshotTakingRule screenshotTakingRule = new ScreenshotTakingRule();
  @Rule
  public RuleChain ruleChain = RuleChain.outerRule(screenshotTakingRule)
      .around(grantPermissionRule)
      .around(activityRule);
  private Resources resources;

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

  @BeforeClass
  public static void setUpOne() {

    Context context = getInstrumentation()
        .getTargetContext().getApplicationContext();

    DemoMode demoMode = new DemoMode(context);
    demoMode.enter();
    demoMode.setClock();
    demoMode.setNetwork();
  }

  @AfterClass
  public static void tearDownOne() {

    Context context = getInstrumentation()
        .getTargetContext().getApplicationContext();
    DemoMode demoMode = new DemoMode(context);
    demoMode.exit();
  }

  private void openNavigationDrawer() {

    onView(withId(R.id.drawer_layout))
        .check(matches(isClosed(Gravity.LEFT)))
        .perform(DrawerActions.open());
  }

  @Before
  public void setUp() {

    TestApp app = (TestApp) getInstrumentation()
        .getTargetContext().getApplicationContext();

    resources = app.getResources();

    app.getAppComponent().inject(this);
  }

  @Test
  public void makeScreenshot_NewEvent() {

    onView(withId(R.id.fab_main_newevent))
        .perform(click());

    onView(withId(R.id.edittext_eventdetail_name))
        .check(matches(isDisplayed()));

    Espresso.closeSoftKeyboard();
  }

  @Test
  public void makeScreenshot_MainView() {

    onView(withId(R.id.fab_main_newevent))
        .check(matches(isDisplayed()));
  }

  @Test
  public void makeScreenshot_ShowEventDetails() {

    onView(withId(R.id.recyclerView))
        .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    onView(withId(R.id.textview_eventdetail_name))
        .check(matches(isDisplayed()));
  }

  @Test
  public void makeScreenshot_ShowMenu() {

    openNavigationDrawer();
  }

  @Test
  public void makeScreenshot_ShowEventOrder() {

    Context context = getInstrumentation().getTargetContext();
    openActionBarOverflowOrOptionsMenu(context);
    onView(withText(R.string.title_order))
        .perform(click());
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
  }
}
