package com.clloret.days.data.repository;

import static org.junit.Assert.assertThat;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import com.clloret.days.data.BuildConfig;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.mapper.DbTagDataMapper;
import com.clloret.days.data.local.repository.RoomTagRepository;
import com.clloret.days.data.remote.entities.mapper.ApiTagDataMapper;
import com.clloret.days.data.remote.repository.AirtableTagRepository;
import com.clloret.days.data.utils.ImmediateSchedulersRule;
import com.clloret.days.data.utils.MediumTest;
import com.clloret.days.data.utils.MockUtils;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.repository.TagRepository;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import java.util.List;
import okhttp3.mockwebserver.MockWebServer;
import org.hamcrest.Matchers;
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
public class AppTagRepositoryTest {

  private static final String API_KEY = "api_key";
  private static final String BASE = "base";

  @Rule
  public final ImmediateSchedulersRule schedulers = new ImmediateSchedulersRule();

  private final MockWebServer server = new MockWebServer();
  private final MockUtils mockUtils = new MockUtils(server);
  private final DbTagDataMapper dbTagDataMapper = new DbTagDataMapper();
  private final ApiTagDataMapper apiTagDataMapper = new ApiTagDataMapper();
  private TagRepository tagRepository;
  private RoomTagRepository roomTagRepository;
  private DaysDatabase db;

  @Before
  public void setUp() throws Exception {

    server.start();
    String serviceEndpoint = server.url("v0").toString();

    Context appContext = RuntimeEnvironment.application;

    db = Room.inMemoryDatabaseBuilder(appContext, DaysDatabase.class)
        .allowMainThreadQueries().build();

    roomTagRepository = new RoomTagRepository(db.tagDao(), dbTagDataMapper);
    AirtableTagRepository airtableTagRepository = new AirtableTagRepository(appContext,
        serviceEndpoint, API_KEY, BASE, apiTagDataMapper);
    tagRepository = new AppTagRepository(roomTagRepository, airtableTagRepository);
  }

  @After
  public void tearDown() throws Exception {

    server.shutdown();
    db.close();
  }

  @Test
  public void getTags_Always_InsertAllValuesInLocalDataStore() throws Exception {

    String fileName = "tags_all.json";
    mockUtils.enqueueMockResponse(200, fileName);

    List<Tag> tags = tagRepository.getAll(true).blockingGet();
    int valueCount = tags.size();

    TestObserver<Tag> testObserver = roomTagRepository.getAll(false)
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
    mockUtils.enqueueMockResponse(200, fileName);

    Tag newTag = newTag(true);

    Tag tag = tagRepository.create(newTag).blockingGet();

    TestObserver<Tag> testObserver = roomTagRepository.getAll(false)
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
    mockUtils.enqueueMockResponse(200, fileName);

    Tag newTag = newTag(false);
    Tag createdTag = roomTagRepository.create(newTag).blockingGet();

    createdTag.setName("Edited tag");

    tagRepository.edit(newTag).blockingGet();

    TestObserver<Tag> testObserver = roomTagRepository.getAll(false)
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
    mockUtils.enqueueMockResponse(200, fileName);

    Tag newTag = newTag(true);
    Tag createdTag = roomTagRepository.create(newTag).blockingGet();

    Boolean deleted = tagRepository.delete(createdTag).blockingGet();

    assertThat(deleted, Matchers.equalTo(true));

    TestObserver<Tag> testObserver = roomTagRepository.getAll(false)
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertNoValues();
  }

}