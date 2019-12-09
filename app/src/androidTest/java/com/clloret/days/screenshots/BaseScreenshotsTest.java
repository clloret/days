package com.clloret.days.screenshots;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
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

  private final ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);
  private final GrantPermissionRule grantPermissionRule = GrantPermissionRule
      .grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
  private final ScreenshotTakingRule screenshotTakingRule = new ScreenshotTakingRule();
  private Resources resources;

  static final Locale DEVICE_LOCALE = new Locale("en", "US");

  @Rule
  public RuleChain ruleChain = RuleChain.outerRule(screenshotTakingRule)
      .around(grantPermissionRule)
      .around(activityRule);

  @Before
  public void setUp() {

    TestApp app = (TestApp) InstrumentationRegistry.getInstrumentation()
        .getTargetContext().getApplicationContext();

    resources = app.getResources();

    app.getAppComponent().inject(this);
  }

  @BeforeClass
  public static void setUpOne() {

    Context context = InstrumentationRegistry.getInstrumentation()
        .getTargetContext().getApplicationContext();

    DemoMode demoMode = new DemoMode(context);
    demoMode.enter();
    demoMode.setClock();
    demoMode.setNetwork();
  }

  @AfterClass
  public static void tearDownOne() {

    Context context = InstrumentationRegistry.getInstrumentation()
        .getTargetContext().getApplicationContext();
    DemoMode demoMode = new DemoMode(context);
    demoMode.exit();
  }

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

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

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

    ViewInteraction appCompatImageButton = onView(
        allOf(withContentDescription(resources.getString(R.string.action_drawer_open)),
            childAtPosition(
                allOf(withId(R.id.toolbar),
                    childAtPosition(
                        withId(R.id.appbar),
                        0)),
                1),
            isDisplayed()));
    appCompatImageButton.perform(click());
  }

  @Test
  public void makeScreenshot_ShowEventOrder() {

    ViewInteraction actionMenuItemView = onView(
        allOf(withContentDescription(resources.getString(R.string.title_order)),
            childAtPosition(
                childAtPosition(
                    withId(R.id.toolbar),
                    2),
                0),
            isDisplayed()));
    actionMenuItemView.perform(click());
  }

  @Test
  public void makeScreenshot_NewTag() {

    ViewInteraction appCompatImageButton = onView(
        allOf(withContentDescription(resources.getString(R.string.action_drawer_open)),
            childAtPosition(
                allOf(withId(R.id.toolbar),
                    childAtPosition(
                        withId(R.id.appbar),
                        0)),
                1),
            isDisplayed()));
    appCompatImageButton.perform(click());

    DataInteraction linearLayout = onData(anything())
        .inAdapterView(allOf(withId(R.id.listView),
            childAtPosition(
                withId(R.id.navigation_drawer),
                0)))
        .atPosition(11);
    linearLayout.perform(click());

    Espresso.closeSoftKeyboard();
  }

  @Test
  public void makeScreenshot_ShowSettings() {

    ViewInteraction appCompatImageButton = onView(
        allOf(withContentDescription(resources.getString(R.string.action_drawer_open)),
            childAtPosition(
                allOf(withId(R.id.toolbar),
                    childAtPosition(
                        withId(R.id.appbar),
                        0)),
                1),
            isDisplayed()));
    appCompatImageButton.perform(click());

    DataInteraction linearLayout = onData(anything())
        .inAdapterView(allOf(withId(R.id.listView),
            childAtPosition(
                withId(R.id.navigation_drawer),
                0)))
        .atPosition(13);
    linearLayout.perform(click());
  }
}
