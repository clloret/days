package com.clloret.days.data.local.entities.dao;

import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.clloret.days.data.local.DaysDatabase;
import com.clloret.days.data.local.entities.DbEvent;
import com.clloret.test_android_common.SampleData;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import java.util.Date;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventDaoTest {

  private static final Date TEST_DATE = new LocalDate()
      .withDayOfMonth(1)
      .withMonthOfYear(6)
      .withYear(2019)
      .toDate();
  private static final String TEST_EVENT_ID = "e0677590-b723-41dd-891b-c3d7da5f157b";
  private static final String TEST_TAG_ID = "99e19dee-306d-4400-9d25-f99cf728493e";

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private EventDao eventDao;
  private DaysDatabase db;

  @Before
  public void setUp() {

    Context context = ApplicationProvider.getApplicationContext();
    db = Room.inMemoryDatabaseBuilder(context, DaysDatabase.class).build();

    SampleData.createEntities(db);

    eventDao = db.eventDao();
  }

  @After
  public void tearDown() {

    db.close();
  }

  @Test
  public void getAll_Always_ReturnAllEvents() {

    TestObserver<DbEvent> testObserver = eventDao.getAll()
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(8);
  }

  @Test
  public void getEventById_Always_ReturnCorrectEvent() {

    DbEvent dbEvent = eventDao.getEventById(TEST_EVENT_ID);

    assertThat(dbEvent.getName()).isEqualTo("New year");
  }

  @Test
  public void loadByTagsIds_Always_ReturnCorrectEvents() {

    TestObserver<DbEvent> testObserver = eventDao
        .loadByTagsIds("%" + TEST_TAG_ID + "%")
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(1);
  }

  @Test
  public void loadWithoutAssignedTags_Always_ReturnCorrectEvents() {

    TestObserver<DbEvent> testObserver = eventDao.loadWithoutAssignedTags()
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(7);
  }

  @Test
  public void loadFavorites_Always_ReturnCorrectEvents() {

    TestObserver<DbEvent> testObserver = eventDao.loadFavorites()
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(1)
        .assertValue(DbEvent::getFavorite);
  }

  @Test
  public void loadBeforeDate_Always_ReturnBeforeDateEvents() {

    TestObserver<DbEvent> testObserver = eventDao.loadBeforeDate(TEST_DATE.getTime())
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(3);
  }

  @Test
  public void loadAfterDate_Always_ReturnAfterDateEvents() {

    TestObserver<DbEvent> testObserver = eventDao.loadAfterDate(TEST_DATE.getTime())
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(4);
  }

  @Test
  public void loadByDate_Always_ReturnEqualDateEvents() {

    TestObserver<DbEvent> testObserver = eventDao.loadByDate(TEST_DATE.getTime())
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(1)
        .assertValue(dbEvent -> TEST_DATE.equals(dbEvent.getDate()));
  }

  @Test
  public void deleteAll_Always_DeleteAllEvents() {

    eventDao.deleteAll();

    TestObserver<DbEvent> testObserver = eventDao.getAll()
        .toObservable()
        .concatMap(Observable::fromIterable)
        .test();

    testObserver
        .assertComplete()
        .assertValueCount(0);
  }

}