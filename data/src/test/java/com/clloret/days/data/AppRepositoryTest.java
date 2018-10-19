package com.clloret.days.data;

import static org.junit.Assert.assertThat;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.LocalDataStore;
import com.clloret.days.data.remote.AirtableDataStore;
import com.clloret.days.data.utils.ImmediateSchedulersRule;
import com.clloret.days.data.utils.MediumTest;
import com.clloret.days.data.utils.RestServiceTestHelper;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.entities.Tag;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@Category({MediumTest.class})
@Config(constants = BuildConfig.class, sdk = VERSION_CODES.M, application = Application.class)
@RunWith(RobolectricTestRunner.class)
public class AppRepositoryTest {

  private static final String API_KEY = "api_key";
  private static final String BASE = "base";

  @Rule
  public final ImmediateSchedulersRule schedulers = new ImmediateSchedulersRule();

  private final MockWebServer server = new MockWebServer();
  private AppRepository appRepository;
  private LocalDataStore localDataStore;
  private DaysDatabase db;

  @Before
  public void setUp() throws Exception {

    server.start();
    String serviceEndpoint = server.url("v0").toString();

    Context appContext = RuntimeEnvironment.application;

    db = Room.inMemoryDatabaseBuilder(appContext, DaysDatabase.class)
        .allowMainThreadQueries().build();

    localDataStore = new LocalDataStore(db);
    AirtableDataStore airtableDataStore = new AirtableDataStore(appContext, serviceEndpoint,
        API_KEY, BASE);
    appRepository = new AppRepository(localDataStore, airtableDataStore);
  }

  @After
  public void tearDown() throws Exception {

    server.shutdown();
    db.close();
  }

  private void enqueueMockResponse(int code, String fileName) throws IOException {

    MockResponse mockResponse = new MockResponse();
    String fileContent = RestServiceTestHelper.readFromInputStream(fileName);
    mockResponse.setResponseCode(code);
    mockResponse.setBody(fileContent);
    server.enqueue(mockResponse);
  }

  @Test
  public void getEvents_Always_InsertAllValuesInLocalDataStore() throws Exception {

    String fileName = "events_all.json";
    enqueueMockResponse(200, fileName);

    List<Event> events = appRepository.getEvents(true).blockingGet();
    int valueCount = events.size();

    TestObserver<Event> testObserver = localDataStore.getEvents(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver.awaitTerminalEvent();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValueCount(valueCount);
  }

  @Test
  public void createEvent_Always_CreateValueInLocalDataStore() throws Exception {

    String fileName = "event_created.json";
    enqueueMockResponse(200, fileName);

    Event newEvent = newEvent(true);

    Event event = appRepository.createEvent(newEvent).blockingGet();

    TestObserver<Event> testObserver = localDataStore.getEvents(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValueCount(1)
        .assertValue(event);
  }

  @Test
  public void editEvent_Always_EditValueInLocalDataStore() throws Exception {

    String fileName = "event_edited.json";
    enqueueMockResponse(200, fileName);

    Event newEvent = newEvent(false);
    Event createdEvent = localDataStore.createEvent(newEvent).blockingGet();

    createdEvent.setName("Edited event");

    appRepository.editEvent(newEvent).blockingGet();

    TestObserver<Event> testObserver = localDataStore.getEvents(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValueCount(1);

    assertThat(testObserver.values(), Matchers.contains(Matchers.allOf(Matchers.hasProperty("name",
        Matchers.is(createdEvent.getName())))));
  }

  @Test
  public void deleteEvent_Always_DeleteValueInLocalDataStore() throws Exception {

    String fileName = "event_deleted.json";
    enqueueMockResponse(200, fileName);

    Event newEvent = newEvent(false);
    Event createdEvent = localDataStore.createEvent(newEvent).blockingGet();

    Boolean deleted = appRepository.deleteEvent(createdEvent).blockingGet();

    assertThat(deleted, Matchers.equalTo(true));

    TestObserver<Event> testObserver = localDataStore.getEvents(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertNoValues();
  }

  @NonNull
  private Event newEvent(boolean nullId) {

    DateTime dt = new DateTime();
    Date date = dt.withYear(2018).withMonthOfYear(1).withDayOfMonth(22).toDate();

    return new EventBuilder()
        .setId(nullId ? null : "rec0XqfldqwfXeoaa")
        .setName("New event")
        .setDescription("Description")
        .setDate(date)
        .build();
  }

  @Test
  public void getTags_Always_InsertAllValuesInLocalDataStore() throws Exception {

    String fileName = "tags_all.json";
    enqueueMockResponse(200, fileName);

    List<Tag> tags = appRepository.getTags(true).blockingGet();
    int valueCount = tags.size();

    TestObserver<Tag> testObserver = localDataStore.getTags(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver.awaitTerminalEvent();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValueCount(valueCount);
  }

  @Test
  public void createTag_Always_CreateValueInLocalDataStore() throws Exception {

    String fileName = "tag_created.json";
    enqueueMockResponse(200, fileName);

    Tag newTag = newTag(true);

    Tag tag = appRepository.createTag(newTag).blockingGet();

    TestObserver<Tag> testObserver = localDataStore.getTags(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValueCount(1)
        .assertValue(tag);
  }

  @NonNull
  private Tag newTag(boolean nullId) {

    return new Tag(nullId ? null : "recPukI2H3ro9UOz9", "New tag");
  }

  @Test
  public void editTag_Always_EditValueInLocalDataStore() throws Exception {

    String fileName = "tag_edited.json";
    enqueueMockResponse(200, fileName);

    Tag newTag = newTag(false);
    Tag createdTag = localDataStore.createTag(newTag).blockingGet();

    createdTag.setName("Edited tag");

    appRepository.editTag(newTag).blockingGet();

    TestObserver<Tag> testObserver = localDataStore.getTags(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValueCount(1);

    assertThat(testObserver.values(), Matchers.contains(Matchers.allOf(Matchers.hasProperty("name",
        Matchers.is(createdTag.getName())))));
  }

  @Test
  public void deleteTag_Always_DeleteValueInLocalDataStore() throws Exception {

    String fileName = "tag_deleted.json";
    enqueueMockResponse(200, fileName);

    Tag newTag = newTag(true);
    Tag createdTag = localDataStore.createTag(newTag).blockingGet();

    Boolean deleted = appRepository.deleteTag(createdTag).blockingGet();

    assertThat(deleted, Matchers.equalTo(true));

    TestObserver<Tag> testObserver = localDataStore.getTags(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertNoValues();
  }

}